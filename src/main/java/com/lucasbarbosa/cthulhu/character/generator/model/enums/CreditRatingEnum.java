package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CreditRatingEnum {
  POOR(1, 9),
  AVERAGE(10, 49),
  WEALTHY(50, 89),
  RICH(90, 98),
  SUPER_RICH(99, 100);

  private Integer minValue;
  private Integer maxValue;
}
