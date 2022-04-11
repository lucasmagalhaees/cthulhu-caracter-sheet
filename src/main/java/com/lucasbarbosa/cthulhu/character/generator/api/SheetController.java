package com.lucasbarbosa.cthulhu.character.generator.api;

import static com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.SheetVO.buildSheet;
import static com.lucasbarbosa.cthulhu.character.generator.model.SkillEnum.CREDIT_RATING;
import static com.lucasbarbosa.cthulhu.character.generator.model.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.SkillEnum.NATIVE_LANGUAGE;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.rollDice;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.shuffle;
import static com.lucasbarbosa.cthulhu.character.generator.util.ApplicationUtils.upperCaseAllFirstCharacter;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SheetVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sheet")
public class SheetController {

  @GetMapping
  public ResponseEntity<SheetVO> getSheet() {
    var characteristics = Arrays.stream(CharacteristicEnum.values()).map(Enum::name)
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
        .filter(assignment -> isFalse(assignment.getIsUsed())).findAny().ifPresent(assignee -> {
              characteristicsVO.add(
                  AttributeVO.buildAttribute(StringUtils.capitalize(attribute.toLowerCase()),
                      assignee.getValue()));
              assignee.setIsUsed(true);
            }
        ));
    characteristicsVO.add(
        AttributeVO.buildAttribute(StringUtils.capitalize("LUCK".toLowerCase()),
            rollDice(2, 6).add(bigDecimalGen(6)).multiply(bigDecimalGen(5)))
    );

    characteristicsVO.add(AttributeVO.buildAttribute(
        StringUtils.capitalize("SANITY".toLowerCase()),
        characteristicsVO.stream()
            .filter(element -> POWER.name().equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(AttributeVO::getMainValue).orElse(
                ZERO)));

    var skills = Arrays.stream(SkillEnum.values())
        .filter(this::hasInitialValue)
        .map(element -> SkillVO.buildSkill(element.name(), element.getInitialValue()))
        .sorted(shuffle())
        .collect(Collectors.toList());
    List<AttributeVO> skillsVO = new ArrayList<>();
    var skillAssignmentVO = Arrays.stream(SkillAssignmentEnum.values()).map(skillAssignment
            -> AssignmentVO.buildAssignment(skillAssignment.name(), skillAssignment.getSkillValue()))
        .sorted(shuffle())
        .collect(Collectors.toList());

    skillAssignmentVO.stream()
        .filter(assignment -> isFalse(assignment.getIsUsed())).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              upperCaseAllFirstCharacter(CREDIT_RATING.name().replace("_", " ").toLowerCase()),
              assignee.getValue()));
          assignee.setIsUsed(true);
        });

    skills.forEach(attribute -> skillAssignmentVO.stream()
        .sorted(shuffle())
        .filter(assignment -> isFalse(assignment.getIsUsed())).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              upperCaseAllFirstCharacter(attribute.getName().replace("_", " ").toLowerCase()),
              attribute.getInitialValue().add(assignee.getValue())));
          assignee.setIsUsed(true);
        }));

    skills.forEach(
        attribute -> skillAssignmentVO.stream().sorted(shuffle())
            .filter(assignment -> isTrue(assignment.getIsUsed()))
            .findAny().ifPresent(assignee -> skillsVO.add(AttributeVO.buildAttribute(
                upperCaseAllFirstCharacter(attribute.getName().replace("_", " ").toLowerCase()),
                attribute.getInitialValue()))));

    skillsVO.add(AttributeVO.buildAttribute(
        upperCaseAllFirstCharacter(DODGE.name().replace("_", " ").toLowerCase()),
        characteristicsVO.stream()
            .filter(element -> DEXTERITY.name().equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(AttributeVO::getHalfValue).orElse(
                ZERO)));

    skillsVO.add(AttributeVO.buildAttribute(
        upperCaseAllFirstCharacter(NATIVE_LANGUAGE.name().replace("_", " ").toLowerCase()),
        characteristicsVO.stream()
            .filter(element -> EDUCATION.name().equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(AttributeVO::getMainValue).orElse(
                ZERO)));

    SheetVO sheetVO = buildSheet(characteristicsVO.stream()
            .sorted(Comparator.comparing(AttributeVO::getMainValue).reversed())
            .collect(Collectors.toList()),
        skillsVO.stream().sorted(Comparator.comparing(AttributeVO::getMainValue).reversed())
            .collect(Collectors.toList()));

    return ResponseEntity.ok(sheetVO);

  }

  private boolean hasInitialValue(SkillEnum value) {
    return isFalse(ZERO.equals(new BigDecimal(value.getInitialValue())));
  }


}
