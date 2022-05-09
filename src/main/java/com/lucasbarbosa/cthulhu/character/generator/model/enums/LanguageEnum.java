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

  PORTUGUESE(true, EASTERN_EUROPE),
  RUSSIAN(true, WESTERN_EUROPE),
  EGYPTIAN(true, AFRICA),
  ARABIC(true, MIDDLE_EAST),
  ENGLISH(true, EASTERN_EUROPE),
  FRENCH(true, EASTERN_EUROPE),
  GERMAN(true, EASTERN_EUROPE),
  AFRICANER(true, AFRICA),
  GREEK(true, EASTERN_EUROPE),
  JAPANESE(true, ASIA),
  MANDARIN(true, ASIA),
  ITALIAN(true, EASTERN_EUROPE),
  DUTCH(true, EASTERN_EUROPE),
  ASTECA(false, SOUTH_AMERICA),
  TUPI(false, SOUTH_AMERICA),
  YORUBA(false, AFRICA),
  NAVAJO(false, NORTH_AMERICA),
  NORSE(false, WESTERN_EUROPE),
  HINDI(false, ASIA),
  BABILONIC(false, MIDDLE_EAST),
  LATIN(false, EASTERN_EUROPE),
  HEBREW(false, MIDDLE_EAST),
  PERSIAN(false, MIDDLE_EAST),
  ARAMAIC(false, MIDDLE_EAST);

  private Boolean isNative;
  private RegionEnum region;

  public static Stream<LanguageEnum> findByRegion(String region) {
    return Arrays.stream(LanguageEnum.values())
        .filter(language -> language.getRegion().toString().equalsIgnoreCase(region));
  }
}
