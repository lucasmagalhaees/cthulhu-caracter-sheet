package com.lucasbarbosa.cthulhu.character.generator.model;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
public class PersonVO {

  private String skillName;
  private String detail;
  private BigDecimal value;

  public static PersonVO buildPerson(String skillName, String detail, BigDecimal value){
    return new PersonVO(skillName, detail, value);
  }


}
