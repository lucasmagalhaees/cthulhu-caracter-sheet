package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum SkillAssignmentEnum {
    FIRST(80),
    SECOND(70),
    THIRD(70),
    FOURTH(60),
    FIFTH(60),
    SIXTH(50),
    SEVENTH(50),
    EIGHTH(50),
    NINTH(40);

    private Integer skillValue;

}
