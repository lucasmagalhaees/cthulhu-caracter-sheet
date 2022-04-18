package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BonusDamageEnum {
  FIRST(2, 64, -2, -2, 0, 0, 0, true),
  SECOND(65, 84, -1, -1, 0, 0, 0, true),
  THIRD(85, 124, 0, 0, 0, 0, 0, true),
  FOURTH(125, 164, 0, 0, 1, 4, 1, false),
  FIFTH(165, 204, 0, 0, 1, 6, 2, false),
  SIXTH(205, 284, 0, 0, 2, 6, 3, false),
  SEVENTH(285, 364, 0, 0, 3, 6, 4, false),
  EIGTH(365, 444, 0, 0, 4, 6, 5, false),
  NINTH(445, 524, 0, 0, 5, 6, 6, false);

  private Integer min;
  private Integer max;
  private Integer signedDamage;
  private Integer signedBuild;
  private Integer dieNumber;
  private Integer dieFaces;
  private Integer build;
  private Boolean isSigned;

}
