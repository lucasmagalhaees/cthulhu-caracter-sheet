package com.lucasbarbosa.cthulhu.character.generator.service;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NATIVE_LANGUAGE;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface CharacterService {

  default RecordVO buildCharacter() {
    List<AttributeVO> characteristicsVO = assignCoreCharacterics();
    assignLuck(characteristicsVO);
    assignSanity(characteristicsVO);

    List<SkillVO> skills = translateSkills();
    List<AttributeVO> skillsVO = new ArrayList<>();
    List<AssignmentVO> skillAssignmentVO = translateSkillAssignment();

    assignCreditRating(skillsVO, skillAssignmentVO);

    assignSkills(skills, skillsVO, skillAssignmentVO);

    buildSkillFromCharacteristic(characteristicsVO, skillsVO, DODGE, DEXTERITY,
        AttributeVO::getHalfValue);
    buildSkillFromCharacteristic(characteristicsVO, skillsVO, NATIVE_LANGUAGE, EDUCATION,
        AttributeVO::getMainValue);

    return buildCharacter(characteristicsVO, skillsVO);

  }

  List<AssignmentVO> translateSkillAssignment();

  List<SkillVO> translateSkills();

  RecordVO buildCharacter(List<AttributeVO> characteristicsVO, List<AttributeVO> skillsVO);

  void buildSkillFromCharacteristic(List<AttributeVO> characteristicsVO,
      List<AttributeVO> skillsVO, SkillEnum skillEnum, MainCharacteristicEnum mainCharacteristicEnum,
      Function<AttributeVO, BigDecimal> converter);

  void assignSkills(List<SkillVO> skills, List<AttributeVO> skillsVO,
      List<AssignmentVO> skillAssignmentVO);


  void assignCreditRating(List<AttributeVO> skillsVO,
      List<AssignmentVO> skillAssignmentVO);

  void assignSanity(List<AttributeVO> characteristicsVO);

  void assignLuck(List<AttributeVO> characteristicsVO);

  List<AttributeVO> assignCoreCharacterics();


}
