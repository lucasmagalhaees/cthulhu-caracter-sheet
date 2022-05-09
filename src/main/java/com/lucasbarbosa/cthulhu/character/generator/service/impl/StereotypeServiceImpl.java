package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.formatAttributeName;

import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.RegionEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.StereotypeEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.StereotypeService;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StereotypeServiceImpl implements StereotypeService {

  @Override
  public List<StereotypeVO> fetchStereotypes() {
    return Arrays.stream(StereotypeEnum.values()).map(setUpStereotype())
        .collect(Collectors.toList());
  }


  @Override
  public List<StereotypeVO> fetchRegions() {
    return Arrays.stream(RegionEnum.values()).map(setUpRegion()).collect(Collectors.toList());
  }

  private Function<StereotypeEnum, StereotypeVO> setUpStereotype() {
    return value -> StereotypeVO.build(value.toString(), formatAttributeName(value.toString()));
  }

  private Function<RegionEnum, StereotypeVO> setUpRegion() {
    return value -> StereotypeVO.build(value.toString(), formatAttributeName(value.toString()));
  }
}
