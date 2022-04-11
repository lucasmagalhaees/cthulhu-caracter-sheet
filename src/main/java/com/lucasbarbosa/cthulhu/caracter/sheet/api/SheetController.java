package com.lucasbarbosa.cthulhu.caracter.sheet.api;

import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.SheetVO.buildSheet;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillEnum.CREDIT_RATING;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillEnum.NATIVE_LANGUAGE;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.bigDecimalGen;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.rollDice;
import static com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils.upperCaseAllFirstCharacter;
import static java.math.BigDecimal.ZERO;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import com.lucasbarbosa.cthulhu.caracter.sheet.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.AttributeVO;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicAssignmentEnum;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.CharacteristicEnum;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.SheetVO;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillAssignmentEnum;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillEnum;
import com.lucasbarbosa.cthulhu.caracter.sheet.model.SkillVO;
import com.lucasbarbosa.cthulhu.caracter.sheet.util.ApplicationUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.BooleanUtils;
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
    var caracteristics = Arrays.stream(CharacteristicEnum.values()).map(Enum::name)
        .collect(Collectors.toList());
    List<AttributeVO> characteristicsVO = new ArrayList<>();
    Collections.shuffle(caracteristics);
    var attributeAssignmentVO = Arrays.stream(CharacteristicAssignmentEnum.values())
        .map(characteristicAssignment
            -> AssignmentVO.buildAssignment(characteristicAssignment.name(),
            characteristicAssignment.getCaracteristicValue())).collect(Collectors.toList());
    caracteristics.forEach(attribute -> attributeAssignmentVO.stream()
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
        .collect(Collectors.toList());
    List<AttributeVO> skillsVO = new ArrayList<>();
    Collections.shuffle(skills);
    var skillAssignmentVO = Arrays.stream(SkillAssignmentEnum.values()).map(skillAssignment
            -> AssignmentVO.buildAssignment(skillAssignment.name(), skillAssignment.getSkillValue()))
        .collect(Collectors.toList());

    skillAssignmentVO.stream()
        .filter(assignment -> isFalse(assignment.getIsUsed())).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              upperCaseAllFirstCharacter(CREDIT_RATING.name().replace("_", " ").toLowerCase()),
              assignee.getValue()));
          assignee.setIsUsed(true);
        });

    skills.forEach(attribute -> skillAssignmentVO.stream()
        .filter(assignment -> isFalse(assignment.getIsUsed())).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              upperCaseAllFirstCharacter(attribute.getName().replace("_", " ").toLowerCase()),
              attribute.getInitialValue().add(assignee.getValue())));
          assignee.setIsUsed(true);
        }));

    skills.forEach(
        attribute -> skillAssignmentVO.stream().filter(assignment -> isTrue(assignment.getIsUsed()))
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

    SheetVO sheetVO = buildSheet(characteristicsVO,
        skillsVO.stream().sorted(Comparator.comparing(AttributeVO::getMainValue).reversed())
            .collect(Collectors.toList()));

    return ResponseEntity.ok(sheetVO);

  }

  private boolean hasInitialValue(SkillEnum value) {
    return isFalse(ZERO.equals(new BigDecimal(value.getInitialValue())));
  }


}
