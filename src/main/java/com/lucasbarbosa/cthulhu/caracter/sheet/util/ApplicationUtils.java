package com.lucasbarbosa.cthulhu.caracter.sheet.util;

import static java.math.BigDecimal.ZERO;

import com.lucasbarbosa.cthulhu.caracter.sheet.model.AttributeVO;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtils {

  public static BigDecimal bigDecimalGen(Integer number) {
    return new BigDecimal(number);
  }

  public static String upperCaseAllFirstCharacter(String text) {
    String regex = "\\b(.)(.*?)\\b";
    return Pattern.compile(regex).matcher(text).replaceAll(
        matche -> matche.group(1).toUpperCase() + matche.group(2)
    );
  }

  public static Integer calculateRandomAge() {
    Random random = new Random();
    return random.nextInt(76) + 15;
  }

  public static <T extends Comparable<T>> boolean isBetween(T value, T start, T end) {
    return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
  }

  public static BigDecimal getCharacteristicMainValue(List<AttributeVO> attributes,
      CharacteristicEnum characteristic) {
    return attributes.stream()
        .filter(element -> characteristic.name().equalsIgnoreCase(element.getAttributeName()))
        .findAny().map(AttributeVO::getMainValue).orElse(
            ZERO);
  }

  public static BigDecimal rollDice(int rollNumber, int diceFace) {
    Random random = new Random();
    var count = 0;
    for (int i = 0; i < rollNumber; i++) {
      var dice = random.nextInt(diceFace - 1) + 1;
      count += dice;
    }
    return new BigDecimal(count);
  }


}
