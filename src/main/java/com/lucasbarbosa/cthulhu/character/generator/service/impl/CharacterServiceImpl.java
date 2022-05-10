package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.formatAttributeName;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.rollDice;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.CREDIT_RATING;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.StereotypeEnum.findSkills;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SupportCharacteristicEnum.LUCK;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SupportCharacteristicEnum.SANITY;
import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.CharacterService;
import com.lucasbarbosa.cthulhu.character.generator.service.RecordService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class CharacterServiceImpl implements CharacterService {

  private final RecordService recordService;

  public CharacterServiceImpl(
      RecordService recordService) {
    this.recordService = recordService;
  }

  @Override
  public void assignSkills(List<SkillVO> skills, List<AttributeVO> skillsVO,
      List<AssignmentVO> skillAssignmentVO) {
    skills.forEach(attribute -> skillAssignmentVO.stream()
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresentOrElse(assignee -> {
          addSkillAttribute(skillsVO, attribute, assignee.getValue());
          assignee.setUsed(true);
        }, () -> addSkillAttribute(skillsVO, attribute, ZERO)));

  }

  @Override
  public void assignCreditRating(List<AttributeVO> skillsVO, List<AssignmentVO> skillAssignmentVO) {
    skillAssignmentVO.stream()
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              formatAttributeName(CREDIT_RATING.name()),
              assignee.getValue()));
          assignee.setUsed(true);
        });
  }

  @Override
  public void assignSanity(List<AttributeVO> characteristicsVO) {
    characteristicsVO.add(AttributeVO.buildAttribute(
        StringUtils.capitalize(SANITY.name().toLowerCase()),
        characteristicsVO.stream()
            .filter(element -> POWER.name().equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(AttributeVO::getMainValue).orElse(
                ZERO)));
  }

  @Override
  public void assignLuck(List<AttributeVO> characteristicsVO) {
    characteristicsVO.add(
        AttributeVO.buildAttribute(
            StringUtils.capitalize(LUCK.name().toLowerCase()),
            rollDice(2, 6).add(bigDecimalGen(6)).multiply(bigDecimalGen(5)))
    );
  }

  private boolean shouldAddChar(List<AttributeVO> attributeVO, String attribute) {
    return attributeVO.stream()
        .map(AttributeVO::getAttributeName)
        .filter(value -> value.equalsIgnoreCase(attribute))
        .findAny().isEmpty();
  }

  @Override
  public void assignCoreCharacterics(List<String> characteristics,
      List<AssignmentVO> attributeAssignmentVO,
      List<AttributeVO> characteristicsVO) {

    characteristics.forEach(attribute -> attributeAssignmentVO.stream()
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresent(assignee -> {
              addChar(characteristicsVO, attribute, assignee);

            }
        ));
  }

  private void addChar(List<AttributeVO> characteristicsVO, String attribute,
      AssignmentVO assignee) {
    if (shouldAddChar(characteristicsVO, attribute)) {

      characteristicsVO.add(
          AttributeVO.buildAttribute(StringUtils.capitalize(attribute.toLowerCase()),
              assignee.getValue()));
      assignee.setUsed(true);
    }
  }

  @Override
  public void assignStereotypeSkills(List<AttributeVO> attributeVO, String stereotype,
      List<AssignmentVO> skillAssignmentVO) {
    var stereotypeSkills = findSkills(stereotype).stream()
        .map(skillAssignment
            -> SkillVO.buildSkill(skillAssignment.name(), skillAssignment.getInitialValue()))
        .collect(Collectors.toList());
    assignSkills(stereotypeSkills, attributeVO, skillAssignmentVO);
  }

  @Override
  public List<AssignmentVO> translateSkillAssignment() {
    return Arrays.stream(SkillAssignmentEnum.values()).map(skillAssignment
            -> AssignmentVO.buildAssignment(skillAssignment.name(), skillAssignment.getSkillValue()))
        .collect(Collectors.toList());
  }

  @Override
  public List<SkillVO> translateSkills() {
    return Arrays.stream(SkillEnum.values())
        .filter(this::hasInitialValue)
        .map(element -> SkillVO.buildSkill(element.name(), element.getInitialValue()))
        .sorted(shuffle())
        .collect(Collectors.toList());
  }

  @Override
  public RecordVO buildCharacter(List<AttributeVO> characteristicsVO,
      List<AttributeVO> skillsVO, String nativeLanguageRegion,
      String foreignLanguageRegion) {
    return recordService.buildRecord(characteristicsVO.stream()
            .sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()),
        skillsVO.stream().sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()), nativeLanguageRegion, foreignLanguageRegion);
  }

  @Override
  public void buildSkillFromCharacteristic(List<AttributeVO> characteristicsVO,
      List<AttributeVO> skillsVO, SkillEnum skillEnum,
      MainCharacteristicEnum mainCharacteristicEnum,
      Function<AttributeVO, BigDecimal> converter) {
    skillsVO.add(AttributeVO.buildAttribute(
        formatAttributeName(skillEnum.name()),
        characteristicsVO.stream()
            .filter(
                element -> mainCharacteristicEnum.name()
                    .equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(converter).orElse(
                ZERO)));
  }

  private void addSkillAttribute(List<AttributeVO> attributeVO, SkillVO skillVO,
      BigDecimal modificator) {
    if (shouldAddSkill(attributeVO, skillVO)) {
      attributeVO.add(AttributeVO.buildAttribute(
          formatAttributeName(skillVO.getName()),
          skillVO.getInitialValue().add(modificator)));
    }

  }

  private boolean shouldAddSkill(List<AttributeVO> attributeVO, SkillVO skillVO) {
    return attributeVO.stream()
        .map(AttributeVO::getAttributeName)
        .filter(attribute -> attribute.equalsIgnoreCase(formatAttributeName(skillVO.getName())))
        .findAny().isEmpty();
  }

  private Comparator<AttributeVO> attributeComparatorFactory(
      Function<AttributeVO, BigDecimal> comparatorParam) {
    return comparing(comparatorParam).reversed();
  }

  private boolean hasInitialValue(SkillEnum value) {
    return isFalse(ZERO.equals(new BigDecimal(value.getInitialValue())));
  }
}
