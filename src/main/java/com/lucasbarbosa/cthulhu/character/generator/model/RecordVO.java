package com.lucasbarbosa.cthulhu.character.generator.model;

import com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils;
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
  private PersonVO nativeLanguage;
  private PersonVO foreignLanguage;
  private PersonVO creditRating;
  private List<AttributeVO> charFirst;
  private List<AttributeVO> charSecond;
  private List<AttributeVO> skillFirst;
  private List<AttributeVO> skillSecond;

  public static RecordVO mountRecord(List<AttributeVO> characterists, List<AttributeVO> skills) {
    var charsSplitted = ApplicationUtils.split(characterists);
    var skillsSplitted = ApplicationUtils.split(skills);

    return RecordVO.builder().charFirst(charsSplitted.get(0)).charSecond(charsSplitted.get(1))
        .skillFirst(skillsSplitted.get(0)).skillSecond(skillsSplitted.get(1))
        .build();
  }

}
