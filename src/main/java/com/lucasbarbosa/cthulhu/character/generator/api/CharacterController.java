package com.lucasbarbosa.cthulhu.character.generator.api;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.getHttpHeaders;

import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.StereotypeVO;
import com.lucasbarbosa.cthulhu.character.generator.service.CharacterService;
import com.lucasbarbosa.cthulhu.character.generator.service.StereotypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random-char")
@Api(tags = "Character Creation")
public class CharacterController {

  private final CharacterService characterService;
  private final StereotypeService stereotypeService;

  public CharacterController(
      CharacterService characterService, StereotypeService stereotypeService) {
    this.characterService = characterService;
    this.stereotypeService = stereotypeService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Resource responsible for ramdomly generating a character")
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
  @ApiOperation(value = "Resource responsible for providing available native regions")
  public ResponseEntity<List<StereotypeVO>> getNativeRegion(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchNativeRegions());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/region/foreign")
  @ApiOperation(value = "Resource responsible for providing available foreign regions")
  public ResponseEntity<List<StereotypeVO>> getForeignRegion(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchForeignRegions());
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/stereotype")
  @ApiOperation(value = "Resource responsible for providing available stereotypes")
  public ResponseEntity<List<StereotypeVO>> getStereotypes(
  ) {
    return ResponseEntity.ok()
        .headers(getHttpHeaders())
        .body(stereotypeService.fetchStereotypes());
  }

}
