package com.lucasbarbosa.cthulhu.character.generator.model;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AssignmentVO {

  @Getter
  private String name;
  @Getter
  private BigDecimal value;

  @Setter
  private Boolean used;

  public Boolean isUsed() {
    return used;
  }

  public static AssignmentVO buildAssignment(String name, Integer number) {
    return new AssignmentVO(name, new BigDecimal(number), false);
  }

}
