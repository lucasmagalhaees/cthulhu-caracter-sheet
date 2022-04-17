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
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.BooleanUtils.isFalse;

import com.lucasbarbosa.cthulhu.character.generator.model.AssignmentVO;
import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.CharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SheetVO;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillAssignmentEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillEnum;
import com.lucasbarbosa.cthulhu.character.generator.model.SkillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sheet")
@Api(tags = "Character Creation")
public class CharacterController {

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Resource responsible for ramdomly generating a character")
  public ResponseEntity<SheetVO> getChar() {
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
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresent(assignee -> {
              characteristicsVO.add(
                  AttributeVO.buildAttribute(StringUtils.capitalize(attribute.toLowerCase()),
                      assignee.getValue()));
              assignee.setUsed(true);
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
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresent(assignee -> {
          skillsVO.add(AttributeVO.buildAttribute(
              upperCaseAllFirstCharacter(CREDIT_RATING.name().replace("_", " ").toLowerCase()),
              assignee.getValue()));
          assignee.setUsed(true);
        });

    skills.forEach(attribute -> skillAssignmentVO.stream()
        .sorted(shuffle())
        .filter(Predicate.not(AssignmentVO::isUsed)).findAny().ifPresentOrElse(assignee -> {
          addSkillAttribute(skillsVO, attribute, assignee.getValue());
          assignee.setUsed(true);
        }, () -> addSkillAttribute(skillsVO, attribute, ZERO)));

    buildSkillFromCharacteristic(characteristicsVO, skillsVO, DODGE, DEXTERITY,
        AttributeVO::getHalfValue);
    buildSkillFromCharacteristic(characteristicsVO, skillsVO, NATIVE_LANGUAGE, EDUCATION,
        AttributeVO::getMainValue);

    SheetVO sheetVO = buildSheet(characteristicsVO.stream()
            .sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()),
        skillsVO.stream().sorted(attributeComparatorFactory(AttributeVO::getMainValue))
            .collect(Collectors.toList()));

    return ResponseEntity.ok(sheetVO);

  }

  private void addSkillAttribute(List<AttributeVO> skillsVO, SkillVO attribute,
      BigDecimal modificator) {
    skillsVO.add(AttributeVO.buildAttribute(
        upperCaseAllFirstCharacter(attribute.getName().replace("_", " ").toLowerCase()),
        attribute.getInitialValue().add(modificator)));
  }

  private Comparator<AttributeVO> attributeComparatorFactory(
      Function<AttributeVO, BigDecimal> comparatorParam) {
    return comparing(comparatorParam).reversed();
  }

  private void buildSkillFromCharacteristic(List<AttributeVO> characteristicsVO,
      List<AttributeVO> skillsVO, SkillEnum skillEnum, CharacteristicEnum characteristicEnum,
      Function<AttributeVO, BigDecimal> converter) {
    skillsVO.add(AttributeVO.buildAttribute(
        upperCaseAllFirstCharacter(skillEnum.name().replace("_", " ").toLowerCase()),
        characteristicsVO.stream()
            .filter(
                element -> characteristicEnum.name().equalsIgnoreCase(element.getAttributeName()))
            .findAny().map(converter).orElse(
                ZERO)));
  }

  private boolean hasInitialValue(SkillEnum value) {
    return isFalse(ZERO.equals(new BigDecimal(value.getInitialValue())));
  }


}
