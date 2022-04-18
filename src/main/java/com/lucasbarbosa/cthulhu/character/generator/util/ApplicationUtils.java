package com.lucasbarbosa.cthulhu.character.generator.util;

import static java.math.BigDecimal.ZERO;

import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtils {

  private static final String UNDERSCORE = "_";
  private static final String BLANK = " ";

  public static BigDecimal bigDecimalGen(Integer number) {
    return new BigDecimal(number);
  }

  public static String formatAttributeName(String text) {
    String regex = "\\b(.)(.*?)\\b";
    return Pattern.compile(regex).matcher(text.replace(UNDERSCORE, BLANK).toLowerCase()).replaceAll(
        matche -> matche.group(1).toUpperCase() + matche.group(2)
    );

  }

  public static <T> Comparator<T> shuffle() {
    final Map<Object, UUID> uniqueIds = new IdentityHashMap<>();
    return Comparator.comparing(e -> uniqueIds.computeIfAbsent(e, k -> UUID.randomUUID()));
  }

  public static Integer calculateRandomAge() {
    Random random = new Random();
    return random.nextInt(76) + 15;
  }

  public static <T extends Comparable<T>> boolean isBetween(T value, T start, T end) {
    return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
  }

  public static BigDecimal getCharacteristicMainValue(List<AttributeVO> attributes,
      MainCharacteristicEnum characteristic) {
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

  public static Function<BigDecimal, Integer> comparetoAttribute(BigDecimal size) {
    return value -> value.compareTo(size);
  }

  public static Predicate<Integer> greaterOrEqualToZero() {
    return value -> value >= 0;
  }

  public static Predicate<Integer> greaterThanZero() {
    return value -> value > 0;
  }

  public static Predicate<Integer> lowerThanZero() {
    return value -> value < 0;
  }


}
