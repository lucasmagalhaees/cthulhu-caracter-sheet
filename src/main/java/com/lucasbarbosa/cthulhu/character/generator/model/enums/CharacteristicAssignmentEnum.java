package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CharacteristicAssignmentEnum {
    FIRST(90),
    SECOND(80),
    THIRD(70),
    FOURTH(60),
    FIFTH(60),
    SIXTH(50),
    SEVENTH(50),
    EIGHTH(40);

    private Integer characteristicValue;

}
