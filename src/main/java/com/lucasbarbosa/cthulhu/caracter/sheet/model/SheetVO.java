package com.lucasbarbosa.cthulhu.caracter.sheet.model;

import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.CONSTITUTION;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.SIZE;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.STRENGTH;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.getCharacteristicMainValue;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.rollDice;
import static java.math.RoundingMode.DOWN;

import com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class SheetVO {

  private BigDecimal hitPoints;
  private BigDecimal magicPoints;

  @Setter
  private Integer damageBonus;

  @Setter
  private Integer build;

  private List<AttributeVO> characterists;
  private List<AttributeVO> skills;

  public static SheetVO buildSheet(List<AttributeVO> characterists, List<AttributeVO> skills) {

    var size = getCharacteristicMainValue(characterists, SIZE);
    var strength = getCharacteristicMainValue(characterists, STRENGTH);

    var hitPoints = getCharacteristicMainValue(characterists, CONSTITUTION).add(
        size).divide(bigDecimalGen(5), 0, DOWN);

    var magicPoints = getCharacteristicMainValue(characterists, POWER).divide(bigDecimalGen(5), 0,
        DOWN);

    SheetVO sheetVO = SheetVO.builder().characterists(characterists).skills(skills)
        .hitPoints(hitPoints)
        .magicPoints(magicPoints)
        .build();

    var bonusParam = size.add(strength);

    var bonusValue = Arrays.stream(BonusEnum.values()).filter(
        bonus -> ApplicationUtils.isBetween(bonusParam, bigDecimalGen(bonus.getMin()),
            bigDecimalGen(bonus.getMax()))).findAny();

    bonusValue.ifPresent(bonus -> {
      if (bonus.getIsSigned()) {
        sheetVO.setDamageBonus(bonus.getSignedDamage());
        sheetVO.setBuild(bonus.getSignedBuild());
      }

      sheetVO.setDamageBonus(rollDice(bonus.getDieNumber(), bonus.getDieFaces()).intValue());
      sheetVO.setBuild(bonus.getBuild());
    });

    return sheetVO;
  }
}
