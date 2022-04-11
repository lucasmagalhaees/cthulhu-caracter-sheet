package com.lucasbarbosa.cthulhu.caracter.sheet.model;

import com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.*;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.*;
import static java.math.RoundingMode.DOWN;

@Getter
@Builder
public class SheetVO {

    private BigDecimal hitPoints;
    private BigDecimal magicPoints;

    @Setter
    private Integer damageBonus;

    @Setter
    private Integer build;

    @Setter
    private Integer movementRate;

    @Setter
    private Integer age;

    private List<AttributeVO> characterists;
    private List<AttributeVO> skills;

    public static SheetVO buildSheet(List<AttributeVO> characterists, List<AttributeVO> skills) {

        var size = getCharacteristicMainValue(characterists, SIZE);
        var strength = getCharacteristicMainValue(characterists, STRENGTH);
        var dexterity = getCharacteristicMainValue(characterists, DEXTERITY);

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

        if (dexterity.compareTo(size) < 0 && strength.compareTo(size) < 0) {
            sheetVO.setMovementRate(7);
        } else if (dexterity.compareTo(size) >= 0 || strength.compareTo(size) >= 0) {
            sheetVO.setMovementRate(8);
        } else if (dexterity.compareTo(size) > 0 && strength.compareTo(size) > 0) {
            sheetVO.setMovementRate(9);
        }

        sheetVO.setAge(ApplicationUtils.calculateRandomAge());

        return sheetVO;
    }
}
