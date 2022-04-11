package com.lucasbarbosa.cthulhu.character.generator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillVO {

    private String name;
    private BigDecimal initialValue;

    public static SkillVO buildSkill(String name, Integer number) {
        return new SkillVO(name, new BigDecimal(number));
    }
}
