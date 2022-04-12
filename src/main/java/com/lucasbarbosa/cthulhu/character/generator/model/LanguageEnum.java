package com.lucasbarbosa.cthulhu.character.generator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LanguageEnum {

  PORTUGUESE(true),
  RUSSIAN(true),
  EGYPTIAN(true),
  ARABIC(true),
  ENGLISH(true),
  FRENCH(true),
  GERMAN(true),
  AFRICANER(true),
  GREEK(true),
  JAPANESE(true),
  MANDARIN(true),
  ITALIAN(true),
  DUTCH(true),
  ASTECA(false),
  TUPI(false),
  YORUBA(false),
  NAVAJO(false),
  NORSE(false),
  HINDI(false),
  BABILONIC(false),
  LATIN(false),
  HEBREW(false),
  PERSIAN(false),
  MONGOL(false),
  ARAMAIC(false);

  private Boolean isNative;
}
