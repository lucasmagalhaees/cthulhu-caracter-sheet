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
  LATIN(false),
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
  BABILONIC(false),
  HEBREW(false),
  PERSIAN(false),
  ARAMAIC(false);

  private Boolean isNative;
}
