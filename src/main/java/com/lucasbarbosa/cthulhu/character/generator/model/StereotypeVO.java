package com.lucasbarbosa.cthulhu.character.generator.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StereotypeVO {

  private String name;
  private String alias;

  public static StereotypeVO build(String name, String alias){
    return new StereotypeVO(name, alias);
  }

}
