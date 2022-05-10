package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.BLANK;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.formatAttributeName;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.joinStringListByComma;

import com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils;
import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.LanguageEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.RegionEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.StereotypeEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.StereotypeService;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StereotypeServiceImpl implements StereotypeService {

  @Override
  public List<StereotypeVO> fetchStereotypes() {
    return Arrays.stream(StereotypeEnum.values()).map(setUpStereotype())
        .collect(Collectors.toList());
  }


  @Override
  public List<StereotypeVO> fetchNativeRegions() {
    return Arrays.stream(RegionEnum.values()).map(setUpNativeRegion()).collect(Collectors.toList());
  }

  @Override
  public List<StereotypeVO> fetchForeignRegions() {
    return Arrays.stream(RegionEnum.values()).map(setUpForeignRegion())
        .collect(Collectors.toList());

  }

  private Function<StereotypeEnum, StereotypeVO> setUpStereotype() {
    return value -> StereotypeVO.build(value.toString(), formatAttributeName(value.toString()),
        "Based on" + BLANK
            + joinStringListByComma(handleStereotypes(value)));
  }

  private List<String> handleStereotypes(StereotypeEnum value) {
    return value.getChars().stream().map(Enum::name).collect(
        Collectors.toList());
  }

  private Function<RegionEnum, StereotypeVO> setUpNativeRegion() {
    return value -> StereotypeVO.build(value.toString(), formatAttributeName(value.toString()),
       "Ex:" + BLANK + joinStringListByComma(handleNativeLanguages(value)));
  }

  private Function<RegionEnum, StereotypeVO> setUpForeignRegion() {
    return value -> StereotypeVO.build(value.toString(), formatAttributeName(value.toString()),
       "Ex:" + BLANK + joinStringListByComma(handleForeignLanguages(value)));
  }

  private List<String> handleNativeLanguages(RegionEnum value) {
    return LanguageEnum.findByRegion(value.name()).filter(LanguageEnum::getIsNative)
        .map(Enum::name).collect(
            Collectors.toList());
  }

  private List<String> handleForeignLanguages(RegionEnum value) {
    return LanguageEnum.findByRegion(value.name()).filter(Predicate.not(LanguageEnum::getIsNative))
        .map(Enum::name).collect(
            Collectors.toList());
  }
}
