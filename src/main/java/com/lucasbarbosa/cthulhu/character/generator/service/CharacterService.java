package com.lucasbarbosa.cthulhu.character.generator.service;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NATIVE_LANGUAGE;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.CharacteristicAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.StereotypeEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface CharacterService {

  default RecordVO build(
      String stereotype,
      String nativeLanguageRegion,
      String foreignLanguageRegion) {
    List<AttributeVO> characteristicsVO = new ArrayList<>();
    var attributeAssignmentVO = Arrays.stream(CharacteristicAssignmentEnum.values())
        .map(characteristicAssignment
            -> AssignmentVO.buildAssignment(characteristicAssignment.name(),
            characteristicAssignment.getCharacteristicValue()))
        .collect(Collectors.toList());
    var stereoTypeCharacteristics = StereotypeEnum.findChars(stereotype).stream().map(Enum::name)
        .sorted(shuffle())
        .collect(Collectors.toList());
    assignCoreCharacterics(stereoTypeCharacteristics, attributeAssignmentVO, characteristicsVO);
    var characteristics = Arrays.stream(MainCharacteristicEnum.values()).map(Enum::name)
        .sorted(shuffle())
        .collect(Collectors.toList());
    assignCoreCharacterics(characteristics, attributeAssignmentVO, characteristicsVO);
    assignLuck(characteristicsVO);
    assignSanity(characteristicsVO);

    List<SkillVO> skillVO = translateSkills();
    List<AttributeVO> attributeVO = new ArrayList<>();
    List<AssignmentVO> skillAssignmentVO = translateSkillAssignment();

    assignCreditRating(attributeVO, skillAssignmentVO);
    assignStereotypeSkills(attributeVO, stereotype, skillAssignmentVO);

    assignSkills(skillVO, attributeVO, skillAssignmentVO);

    buildSkillFromCharacteristic(characteristicsVO, attributeVO, DODGE, DEXTERITY,
        AttributeVO::getHalfValue);
    buildSkillFromCharacteristic(characteristicsVO, attributeVO, NATIVE_LANGUAGE, EDUCATION,
        AttributeVO::getMainValue);

    return buildCharacter(characteristicsVO, attributeVO, nativeLanguageRegion,
        foreignLanguageRegion);

  }

  void assignStereotypeSkills(List<AttributeVO> attributeVO, String stereotype,
      List<AssignmentVO> skillAssignmentVO);

  List<AssignmentVO> translateSkillAssignment();

  List<SkillVO> translateSkills();

  RecordVO buildCharacter(List<AttributeVO> characteristicsVO, List<AttributeVO> attributeVO,
      String nativeLanguageRegion, String foreignLanguageRegion);

  void buildSkillFromCharacteristic(List<AttributeVO> characteristicsVO,
      List<AttributeVO> attributeVO, SkillEnum skillEnum,
      MainCharacteristicEnum mainCharacteristicEnum,
      Function<AttributeVO, BigDecimal> converter);

  void assignSkills(List<SkillVO> skills, List<AttributeVO> attributeVO,
      List<AssignmentVO> skillAssignmentVO);


  void assignCreditRating(List<AttributeVO> attributeVO,
      List<AssignmentVO> skillAssignmentVO);

  void assignSanity(List<AttributeVO> characteristicsVO);

  void assignLuck(List<AttributeVO> characteristicsVO);

  void assignCoreCharacterics(
      List<String> characteristics,
      List<AssignmentVO> attributeAssignmentVO,
      List<AttributeVO> characteristicsVO);


}
