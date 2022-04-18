package com.lucasbarbosa.cthulhu.character.generator.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class RecordVO {

  private BigDecimal hitPoints;
  private Integer age;
  private Integer movementRate;
  private Integer build;
  private Integer bonusDamage;
  private BigDecimal magicPoints;
  private String nativeLanguage;
  private String foreignLanguage;
  private List<AttributeVO> characteristics;
  private List<AttributeVO> skills;

  public static RecordVO mountRecord(List<AttributeVO> characterists, List<AttributeVO> skills){
    return RecordVO.builder().characteristics(characterists).skills(skills)
        .build();
  }

}
