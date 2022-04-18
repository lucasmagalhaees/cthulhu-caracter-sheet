package com.lucasbarbosa.cthulhu.character.generator.service.impl;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.CREDIT_RATING;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SupportCharacteristicEnum.LUCK;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SupportCharacteristicEnum.SANITY;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.rollDice;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.formatAttributeName;
import static java.math.BigDecimal.ZERO;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.CharacteristicAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.CharacterService;
import com.lucasbarbosa.cthulhu.character.generator.service.RecordService;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        .sorted(shuffle())
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

  @Override
  public List<AttributeVO> assignCoreCharacterics() {
    var characteristics = Arrays.stream(MainCharacteristicEnum.values()).map(Enum::name)
        .sorted(shuffle())
        .collect(Collectors.toList());
    List<AttributeVO> characteristicsVO = new ArrayList<>();
    var attributeAssignmentVO = Arrays.stream(CharacteristicAssignmentEnum.values())
        .map(characteristicAssignment
            -> AssignmentVO.buildAssignment(characteristicAssignment.name(),
            characteristicAssignment.getCharacteristicValue()))
        .sorted(shuffle())
        .collect(Collectors.toList());
    characteristics.forEach(attribute -> attributeAssignmentVO.stream()
        .sorted(shuffle())
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresent(assignee -> {
              characteristicsVO.add(
                  AttributeVO.buildAttribute(StringUtils.capitalize(attribute.toLowerCase()),
                      assignee.getValue()));
              assignee.setUsed(true);
            }
        ));
    return characteristicsVO;
  }

  @Override
  public List<AssignmentVO> translateSkillAssignment() {
    return Arrays.stream(SkillAssignmentEnum.values()).map(skillAssignment
            -> AssignmentVO.buildAssignment(skillAssignment.name(), skillAssignment.getSkillValue()))
        .sorted(shuffle())
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
      List<AttributeVO> skillsVO) {
    return recordService.buildRecord(characteristicsVO.stream()
            .sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()),
        skillsVO.stream().sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()));
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

  private void addSkillAttribute(List<AttributeVO> skillsVO, SkillVO attribute,
      BigDecimal modificator) {
    skillsVO.add(AttributeVO.buildAttribute(
        formatAttributeName(attribute.getName()),
        attribute.getInitialValue().add(modificator)));
  }

  private Comparator<AttributeVO> attributeComparatorFactory(
      Function<AttributeVO, BigDecimal> comparatorParam) {
    return comparing(comparatorParam).reversed();
  }

  private boolean hasInitialValue(SkillEnum value) {
    return isFalse(ZERO.equals(new BigDecimal(value.getInitialValue())));
  }
}
