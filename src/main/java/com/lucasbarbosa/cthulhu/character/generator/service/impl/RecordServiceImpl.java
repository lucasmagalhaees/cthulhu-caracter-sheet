package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.comparetoAttribute;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.getCharacteristicMainValue;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.greaterOrEqualToZero;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.greaterThanZero;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.isBetween;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.lowerThanZero;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.LanguageEnum.findByRegion;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.CONSTITUTION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.SIZE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.STRENGTH;
import static java.lang.Boolean.TRUE;
import static java.math.RoundingMode.DOWN;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.capitalize;

import com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.LanguageVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.BonusDamageEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.LanguageEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.RecordService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

  @Override
  public void evaluateHitPoints(Map<MainCharacteristicEnum, BigDecimal> coreStats,
      RecordVO recordVO) {
    var hitPoints = coreStats.get(CONSTITUTION).add(
        coreStats.get(SIZE)).divide(bigDecimalGen(5), 0, DOWN);
    recordVO.setHitPoints(hitPoints);
  }

  @Override
  public void evaluateMagicPoints(Map<MainCharacteristicEnum, BigDecimal> coreStats,
      RecordVO recordVO) {
    var magicPoints = coreStats.get(POWER).divide(bigDecimalGen(5), 0,
        DOWN);
    recordVO.setMagicPoints(magicPoints);
  }

  @Override
  public void assignCharacterLanguages(RecordVO recordVO, String nativeLanguageRegion,
      String foreignLanguageRegion) {
    var languagesAvailable = Arrays.stream(LanguageEnum.values()).map(language
            -> LanguageVO.buildLanguage(language.name(), language.getIsNative()))
        .sorted(shuffle())
        .collect(Collectors.toList());

    setRandomNative(recordVO, languagesAvailable);
    setSpecifiedNative(recordVO, nativeLanguageRegion);
    setRandomForeign(recordVO, languagesAvailable);
    setSpecifiedForeign(recordVO, foreignLanguageRegion);
  }

  private void setRandomForeign(RecordVO recordVO, List<LanguageVO> languagesAvailable) {
    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(Predicate.not(LanguageVO::isNative))
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(
            assignee -> recordVO.setForeignLanguage(capitalize(assignee.getName().toLowerCase())));
  }

  private void setSpecifiedForeign(RecordVO recordVO, String nativeLanguageRegion) {
    findByRegion(nativeLanguageRegion).sorted(shuffle())
        .map(language
            -> LanguageVO.buildLanguage(language.name(), language.getIsNative()))
        .filter(Predicate.not(LanguageVO::isNative))
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(
            assignee -> recordVO.setForeignLanguage(capitalize(assignee.getName().toLowerCase())));
  }

  private void setSpecifiedNative(RecordVO recordVO, String nativeLanguageRegion) {
    findByRegion(nativeLanguageRegion).sorted(shuffle())
        .map(language
            -> LanguageVO.buildLanguage(language.name(), language.getIsNative()))
        .filter(LanguageVO::isNative)
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(
            assignee -> recordVO.setNativeLanguage(capitalize(assignee.getName().toLowerCase())));
  }

  private void setRandomNative(RecordVO recordVO, List<LanguageVO> languagesAvailable) {
    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(LanguageVO::isNative)
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(
            assignee -> recordVO.setNativeLanguage(capitalize(assignee.getName().toLowerCase())));
  }

  @Override
  public void assignBonusDamage(RecordVO recordVO, BigDecimal bonusParam) {
    var bonusDamage = Arrays.stream(BonusDamageEnum.values()).filter(
        bonus -> isBetween(bonusParam,
            bigDecimalGen(bonus.getMin()),
            bigDecimalGen(bonus.getMax()))).findAny();

    bonusDamage.ifPresent(bonus -> {
      if (TRUE.equals(bonus.getIsSigned())) {
        recordVO.setBonusDamage(bonus.getSignedDamage());
        recordVO.setBuild(bonus.getSignedBuild());
      } else {
        recordVO.setBonusDamage(
            ApplicationUtils.rollDice(bonus.getDieNumber(), bonus.getDieFaces()).intValue());
        recordVO.setBuild(bonus.getBuild());
      }

    });
  }

  @Override
  public Map<MainCharacteristicEnum, BigDecimal> buildCoreStats(List<AttributeVO> characterists) {
    Map<MainCharacteristicEnum, BigDecimal> coreStats = new HashMap<>();
    coreStats.put(SIZE, getCharacteristicMainValue(characterists, SIZE));
    coreStats.put(STRENGTH, getCharacteristicMainValue(characterists, STRENGTH));
    coreStats.put(DEXTERITY, getCharacteristicMainValue(characterists, DEXTERITY));
    coreStats.put(CONSTITUTION, getCharacteristicMainValue(characterists, CONSTITUTION));
    coreStats.put(POWER, getCharacteristicMainValue(characterists, POWER));
    return coreStats;
  }

  @Override
  public void evaluateMovementRate(RecordVO recordVO,
      Map<MainCharacteristicEnum, BigDecimal> coreStats) {
    var dexterity = coreStats.get(DEXTERITY);
    var size = coreStats.get(SIZE);
    var strength = coreStats.get(STRENGTH);

    if (Stream.of(dexterity, strength).map(comparetoAttribute(size))
        .allMatch(lowerThanZero())) {
      recordVO.setMovementRate(7);
    } else if (Stream.of(dexterity, strength).map(comparetoAttribute(size))
        .anyMatch(greaterOrEqualToZero())) {
      recordVO.setMovementRate(8);
    } else if (Stream.of(dexterity, strength).map(comparetoAttribute(size))
        .allMatch(greaterThanZero())) {
      recordVO.setMovementRate(9);
    }
  }

}
