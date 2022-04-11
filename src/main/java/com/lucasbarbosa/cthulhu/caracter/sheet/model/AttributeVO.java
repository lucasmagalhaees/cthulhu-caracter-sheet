package com.lucasbarbosa.cthulhu.caracter.sheet.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.bigDecimalGen;

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

    public static AttributeVO buildAttribute(String attributeName, BigDecimal mainValue) {
        return new AttributeVO(attributeName, mainValue);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public BigDecimal getMainValue() {
        return mainValue;
    }

    public BigDecimal getHalfValue() {
        return mainValue.divide(bigDecimalGen(2), RoundingMode.HALF_UP);
    }

    public BigDecimal getFifthValue() {
        return mainValue.divide(bigDecimalGen(5), RoundingMode.HALF_UP);
    }
}
