package com.lucasbarbosa.cthulhu.character.generator.api;

import static java.util.Comparator.comparing;

import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.service.CharacterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random-char")
@Api(tags = "Character Creation")
public class CharacterController {

  private final CharacterService characterService;

  public CharacterController(
      CharacterService characterService) {
    this.characterService = characterService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Resource responsible for ramdomly generating a character")
  public ResponseEntity<RecordVO> getRandomChar() {

    return ResponseEntity.ok(characterService.buildCharacter());

  }


}
