package com.lucasbarbosa.cthulhu.caracter.sheet.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AssignmentVO {

    private String name;
    private BigDecimal value;

    @Setter
    private Boolean isUsed;

    public static AssignmentVO buildAssignment(String name, Integer number) {
        return new AssignmentVO(name, new BigDecimal(number), false);
    }

}
