package com.lucasbarbosa.cthulhu.character.generator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LanguageVO {

  @Getter
  private String name;

  private Boolean nativeLanguage;

  @Setter
  private Boolean used;

  public Boolean isNative() {
    return nativeLanguage;
  }

  public Boolean isUsed() {
    return used;
  }

  public static LanguageVO buildLanguage(String name, Boolean isNative) {
    return new LanguageVO(name, isNative, false);
  }

}
