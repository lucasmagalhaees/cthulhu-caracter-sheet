package com.lucasbarbosa.cthulhu.character.generator.model;

import com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AttributeVO {

  private String attributeName;
  private BigDecimal mainValue;
  private BigDecimal halfValue;
  private BigDecimal fifthValue;

  private AttributeVO(String attributeName, BigDecimal mainValue) {
    this.attributeName = attributeName;
    this.mainValue = mainValue;
    this.halfValue = getHalfValue();
    this.fifthValue = getFifthValue();
  }

  public static AttributeVO buildAttribute(String attributeName,
      BigDecimal mainValue) {
    return new AttributeVO(attributeName, mainValue);
  }

  public String getAttributeName() {
    return attributeName;
  }

  public BigDecimal getMainValue() {
    return mainValue;
  }

  public BigDecimal getHalfValue() {
    return mainValue.divide(ApplicationUtils.bigDecimalGen(0), RoundingMode.DOWN);
  }

  public BigDecimal getFifthValue() {
    return mainValue.divide(ApplicationUtils.bigDecimalGen(0), RoundingMode.DOWN);
  }

}
