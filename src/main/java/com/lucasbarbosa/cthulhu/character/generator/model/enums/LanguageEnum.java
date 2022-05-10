package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.RegionEnum.*;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.RegionEnum.EASTERN_EUROPE;

import java.util.Arrays;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LanguageEnum {

  ARABIC(true, MIDDLE_EAST),
  TURKISH(true, MIDDLE_EAST),
  PERSIAN(false, MIDDLE_EAST),
  HEBREW(false, MIDDLE_EAST),
  ENGLISH(true, WESTERN_EUROPE),
  FRENCH(true, WESTERN_EUROPE),
  GERMAN(true, WESTERN_EUROPE),
  DUTCH(true, WESTERN_EUROPE),
  ITALIAN(true, WESTERN_EUROPE),
  GREEK(false, WESTERN_EUROPE),
  LATIN(false, WESTERN_EUROPE),
  CELTIC(false, WESTERN_EUROPE),
  AFRICANER(true, AFRICA),
  ZULU(true, AFRICA),
  UBUNDU(true, AFRICA),
  CRIOLO(false, AFRICA),
  YORUBA(false, AFRICA),
  EGYPTIAN(false, AFRICA),
  JAPANESE(true, ASIA),
  THAI(true, ASIA),
  KOREAN(true, ASIA),
  MANDARIN(true, ASIA),
  HINDI(false, ASIA),
  MONGOLIAN(false, ASIA),
  SPANISH(true, AMERICA),
  PORTUGUESE(true, AMERICA),
  ASTECA(false, AMERICA),
  INCA(false, AMERICA),
  MAYA(false, AMERICA),
  TUPI(false, AMERICA),
  NAVAJO(false, AMERICA),
  RUSSIAN(true, EASTERN_EUROPE),
  ROMANIAN(true, EASTERN_EUROPE),
  SERBIAN(true, EASTERN_EUROPE),
  CZECH(true, EASTERN_EUROPE),
  SLAV(false, EASTERN_EUROPE),
  GYPSY(false, EASTERN_EUROPE),
  NORSE(false, EASTERN_EUROPE);

  private Boolean isNative;
  private RegionEnum region;

  public static Stream<LanguageEnum> findByRegion(String region) {
    return Arrays.stream(LanguageEnum.values())
        .filter(language -> language.getRegion().toString().equalsIgnoreCase(region));
  }
}
