package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.CONSTITUTION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.SIZE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.STRENGTH;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.comparetoAttribute;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.getCharacteristicMainValue;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.greaterOrEqualToZero;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.greaterThanZero;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.isBetween;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.lowerThanZero;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.shuffle;
import static java.lang.Boolean.TRUE;
import static java.math.RoundingMode.DOWN;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.capitalize;

import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.LanguageVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.BonusDamageEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.LanguageEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.RecordService;
import com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl implements RecordService {

  @Override
  public void evaluateHitPoints(Map<MainCharacteristicEnum, BigDecimal> coreStats, RecordVO recordVO) {
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
  public void assignCharacterLanguages(RecordVO recordVO) {
    var languagesAvailable = Arrays.stream(LanguageEnum.values()).map(language
            -> LanguageVO.buildLanguage(language.name(), language.getIsNative()))
        .sorted(shuffle())
        .collect(Collectors.toList());

    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(LanguageVO::isNative)
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(assignee -> {
          recordVO.setNativeLanguage(capitalize(assignee.getName().toLowerCase()));
          assignee.setUsed(true);
        });

    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(Predicate.not(LanguageVO::isNative))
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(assignee -> {
          recordVO.setForeignLanguage(capitalize(assignee.getName().toLowerCase()));
          assignee.setUsed(true);
        });
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
