package com.lucasbarbosa.cthulhu.character.generator.model;

import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.shuffle;
import static java.lang.Boolean.TRUE;
import static java.math.RoundingMode.DOWN;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.capitalize;

import com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class SheetVO {

  private BigDecimal hitPoints;
  private Integer age;
  private Integer movementRate;
  private Integer build;
  private Integer damageBonus;
  private BigDecimal magicPoints;
  private String nativeLanguage;
  private String foreignLanguage;
  private List<AttributeVO> characteristics;
  private List<AttributeVO> skills;

  public static SheetVO buildSheet(List<AttributeVO> characterists, List<AttributeVO> skills) {

    var size = ApplicationUtils.getCharacteristicMainValue(characterists, CharacteristicEnum.SIZE);
    var strength = ApplicationUtils.getCharacteristicMainValue(characterists,
        CharacteristicEnum.STRENGTH);
    var dexterity = ApplicationUtils.getCharacteristicMainValue(characterists,
        CharacteristicEnum.DEXTERITY);

    var hitPoints = ApplicationUtils.getCharacteristicMainValue(characterists,
        CharacteristicEnum.CONSTITUTION).add(
        size).divide(ApplicationUtils.bigDecimalGen(5), 0, DOWN);

    var magicPoints = ApplicationUtils.getCharacteristicMainValue(characterists,
        CharacteristicEnum.POWER).divide(ApplicationUtils.bigDecimalGen(5), 0,
        DOWN);

    SheetVO sheetVO = SheetVO.builder().characteristics(characterists).skills(skills)
        .hitPoints(hitPoints)
        .magicPoints(magicPoints)
        .build();

    var bonusParam = size.add(strength);

    var bonusValue = Arrays.stream(BonusEnum.values()).filter(
        bonus -> ApplicationUtils.isBetween(bonusParam,
            ApplicationUtils.bigDecimalGen(bonus.getMin()),
            ApplicationUtils.bigDecimalGen(bonus.getMax()))).findAny();

    bonusValue.ifPresent(bonus -> {
      if (TRUE.equals(bonus.getIsSigned())) {
        sheetVO.setDamageBonus(bonus.getSignedDamage());
        sheetVO.setBuild(bonus.getSignedBuild());
      }

      sheetVO.setDamageBonus(
          ApplicationUtils.rollDice(bonus.getDieNumber(), bonus.getDieFaces()).intValue());
      sheetVO.setBuild(bonus.getBuild());
    });

    if (dexterity.compareTo(size) < 0 && strength.compareTo(size) < 0) {
      sheetVO.setMovementRate(7);
    } else if (dexterity.compareTo(size) >= 0 || strength.compareTo(size) >= 0) {
      sheetVO.setMovementRate(8);
    } else if (dexterity.compareTo(size) > 0 && strength.compareTo(size) > 0) {
      sheetVO.setMovementRate(9);
    }

    sheetVO.setAge(ApplicationUtils.calculateRandomAge());

    var languagesAvailable = Arrays.stream(LanguageEnum.values()).map(skillAssignment
            -> AssignmentVO.buildAssignment(skillAssignment.name(),
            Optional.of(skillAssignment).filter(LanguageEnum::getIsNative).map(value -> 0)
                .orElse(1)))
        .sorted(shuffle())
        .collect(Collectors.toList());

    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(SheetVO::isNative)
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(assignee -> {
          sheetVO.setNativeLanguage(capitalize(assignee.getName().toLowerCase()));
          assignee.setUsed(true);
        });

    languagesAvailable.stream()
        .sorted(shuffle())
        .filter(Predicate.not(SheetVO::isNative))
        .filter(assignment -> isFalse(assignment.isUsed())).findAny().ifPresent(assignee -> {
          sheetVO.setForeignLanguage(capitalize(assignee.getName().toLowerCase()));
          assignee.setUsed(true);
        });

    return sheetVO;
  }

  private static boolean isNative(AssignmentVO element) {
    return BigDecimal.ZERO.equals(element.getValue());
  }
}
