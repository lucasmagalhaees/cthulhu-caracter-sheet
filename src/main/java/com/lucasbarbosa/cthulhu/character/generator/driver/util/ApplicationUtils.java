package com.lucasbarbosa.cthulhu.character.generator.driver.util;

import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.LanguageEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.RegionEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.StereotypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationUtils {

  private static final String UNDERSCORE = "_";
  private static final String BLANK = " ";
  private static final String COMMA = ", ";
  private static final String ENUM_ASSURANCE_MESSAGE = "field %s must be any of %s";

  public static BigDecimal bigDecimalGen(Integer number) {
    return new BigDecimal(number);
  }

  public static String formatAttributeName(String text) {
    String regex = "\\b(.)(.*?)\\b";
    return Pattern.compile(regex).matcher(text.replace(UNDERSCORE, BLANK).toLowerCase()).replaceAll(
        matche -> matche.group(1).toUpperCase() + matche.group(2)
    );

  }

  public static String joinStringListByComma(List<String> stringList) {
    return String.join(COMMA, stringList);
  }

  public static String getEnumAssuranceMessage() {
    return ENUM_ASSURANCE_MESSAGE;
  }

  public static String convertObjectToString(Object object) {
    return Optional.ofNullable(object).map(Objects::toString).orElse(EMPTY);
  }

  public static List<String> convertEnumToStringList(Class<? extends Enum<?>> enumeration) {
    return Stream.of(enumeration.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
  }

  public static List<List<AttributeVO>> split(List<AttributeVO> list)
  {
    int midIndex
        = ((list.size() / 2)
        - (((list.size() % 2) > 0) ? 0 : 1));

    return new ArrayList<>(
        list.stream()
            .collect(Collectors.partitioningBy(
                s -> list.indexOf(s) > midIndex))
            .values());

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

  public static HttpHeaders getHttpHeaders() {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Access-Control-Allow-Origin", "*");
    return responseHeaders;
  }
}
