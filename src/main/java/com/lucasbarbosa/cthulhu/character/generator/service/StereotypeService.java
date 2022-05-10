package com.lucasbarbosa.cthulhu.character.generator.service;

import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import java.util.List;

public interface StereotypeService {

  List<StereotypeVO> fetchStereotypes();

  List<StereotypeVO> fetchNativeRegions();
  List<StereotypeVO> fetchForeignRegions();

}
