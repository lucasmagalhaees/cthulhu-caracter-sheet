package com.lucasbarbosa.cthulhu.character.generator.service;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.sortCollection;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FOREIGN_LANGUAGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NATIVE_LANGUAGE;
import static java.util.stream.Collectors.toList;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.PersonVO;
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
        .collect(toList());
    var stereoTypeCharacteristics = StereotypeEnum.findChars(stereotype).stream().map(Enum::name)
        .collect(toList());
    assignCoreCharacterics(stereoTypeCharacteristics, attributeAssignmentVO, characteristicsVO);
    var characteristics = Arrays.stream(MainCharacteristicEnum.values()).map(Enum::name)
        .collect(toList());
    assignCoreCharacterics(sortCollection(characteristics), sortCollection(attributeAssignmentVO) , characteristicsVO);
    assignLuck(characteristicsVO);
    assignSanity(characteristicsVO);

    List<SkillVO> skillVO = translateSkills();
    List<AttributeVO> attributeVO = new ArrayList<>();
    List<AssignmentVO> skillAssignmentVO = translateSkillAssignment();

    var creditRating = new PersonVO();
    assignCreditRating(attributeVO, creditRating);
    assignStereotypeSkills(attributeVO, stereotype, skillAssignmentVO);

    assignSkills(skillVO, attributeVO, skillAssignmentVO);

    buildSkillFromCharacteristic(characteristicsVO, attributeVO, DODGE, DEXTERITY,
        AttributeVO::getHalfValue);
    buildSkillFromCharacteristic(characteristicsVO, attributeVO, NATIVE_LANGUAGE, EDUCATION,
        AttributeVO::getMainValue);
    buildSkillFromCharacteristic(characteristicsVO, attributeVO, FOREIGN_LANGUAGE, EDUCATION,
        AttributeVO::getHalfValue);

    return buildCharacter(characteristicsVO, attributeVO, nativeLanguageRegion,
        foreignLanguageRegion, creditRating);

  }

  void assignStereotypeSkills(List<AttributeVO> attributeVO, String stereotype,
      List<AssignmentVO> skillAssignmentVO);

  List<AssignmentVO> translateSkillAssignment();

  List<SkillVO> translateSkills();

  RecordVO buildCharacter(List<AttributeVO> characteristicsVO, List<AttributeVO> attributeVO,
      String nativeLanguageRegion, String foreignLanguageRegion,
      PersonVO creditRating);

  void buildSkillFromCharacteristic(List<AttributeVO> characteristicsVO,
      List<AttributeVO> attributeVO, SkillEnum skillEnum,
      MainCharacteristicEnum mainCharacteristicEnum,
      Function<AttributeVO, BigDecimal> converter);

  void assignSkills(List<SkillVO> skills, List<AttributeVO> attributeVO,
      List<AssignmentVO> skillAssignmentVO);


  void assignCreditRating(List<AttributeVO> attributeVO,
      PersonVO creditRating);

  void assignSanity(List<AttributeVO> characteristicsVO);

  void assignLuck(List<AttributeVO> characteristicsVO);

  void assignCoreCharacterics(
      List<String> characteristics,
      List<AssignmentVO> attributeAssignmentVO,
      List<AttributeVO> characteristicsVO);


}
