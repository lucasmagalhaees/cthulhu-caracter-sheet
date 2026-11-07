package com.lucasbarbosa.cthulhu.character.generator.api;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.getHttpHeaders;

import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import com.lucasbarbosa.cthulhu.character.generator.service.CharacterService;
import com.lucasbarbosa.cthulhu.character.generator.service.StereotypeService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random-char")
public class CharacterController {

  private final CharacterService characterService;
  private final StereotypeService stereotypeService;

  public CharacterController(
      CharacterService characterService, StereotypeService stereotypeService) {
    this.characterService = characterService;
    this.stereotypeService = stereotypeService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RecordVO> getRandomChar(
      @RequestParam(name = "stereotype", required = false)  String stereotype,
      @RequestParam(name = "nativeLanguageRegion", required = false)  String nativeLanguageRegion,
      @RequestParam(name = "foreignLanguageRegion", required = false)  String foreignLanguageRegion

  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(characterService.build(stereotype, nativeLanguageRegion, foreignLanguageRegion));
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/region/native")
  public ResponseEntity<List<StereotypeVO>> getNativeRegion(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchNativeRegions());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/region/foreign")
  public ResponseEntity<List<StereotypeVO>> getForeignRegion(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchForeignRegions());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/stereotype")
  public ResponseEntity<List<StereotypeVO>> getStereotypes(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchStereotypes());
  }

}
