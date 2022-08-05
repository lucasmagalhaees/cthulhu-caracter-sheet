package com.lucasbarbosa.cthulhu.character.generator.service;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DODGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FOREIGN_LANGUAGE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NATIVE_LANGUAGE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import com.lucasbarbosa.cthulhu.character.generator.service.impl.CharacterServiceImpl;
import com.lucasbarbosa.cthulhu.character.generator.service.impl.RecordServiceImpl;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CharacterServiceTestSupport {

  @Mock
  private CharacterService characterService;
  @Mock
  private RecordService recordService;
  Map<MainCharacteristicEnum, BigDecimal> coreStats;
  protected RecordVO sheet;
  protected List<AttributeVO> characteristics;
  protected List<AttributeVO> skills;
  protected String attributes;

  public void setCoreStats(
      Map<MainCharacteristicEnum, BigDecimal> coreStats) {
    this.coreStats = coreStats;
  }

  public void setSheet(RecordVO sheet) {
    this.sheet = sheet;
  }

  public void setCharacteristics(
      List<AttributeVO> characteristics) {
    this.characteristics = characteristics;
  }

  public void setSkills(
      List<AttributeVO> skills) {
    this.skills = skills;
  }

  public void setAttributes(String attributes) {
    this.attributes = attributes;
  }

  @BeforeEach
  void initTest() {
    MockitoAnnotations.openMocks(this);
    recordService = new RecordServiceImpl();
    characterService = new CharacterServiceImpl(recordService);
  }

  void buildSheetWithoutParams() {
    setSheet(characterService.build(EMPTY, EMPTY, EMPTY));
  }

  void runTest() {
    setAttributes(Arrays.stream(MainCharacteristicEnum.values())
        .map(MainCharacteristicEnum::name)
        .collect(Collectors.joining(",")));
    setCharacteristics(Stream.of(sheet.getCharFirst(), sheet.getCharSecond())
        .flatMap(Collection::stream)
        .collect(toList()));
    setSkills(Stream.of(sheet.getSkillFirst(), sheet.getSkillSecond())
        .flatMap(Collection::stream)
        .collect(toList()));
    setCoreStats(recordService.buildCoreStats(
        characteristics)
    );
  }

  protected void assertThatForeignLanguageIsHalfOfEducation() {
    assertThat(coreStats.get(MainCharacteristicEnum.EDUCATION).intValue())
        .isEqualTo(fetchSkillMainValue(skills, FOREIGN_LANGUAGE.name().replace("_", " ")) * 2);
  }

  protected void assertThatNativeLanguageIsTheSameAsEducation() {
    assertThat(coreStats.get(MainCharacteristicEnum.EDUCATION).intValue())
        .isEqualTo(fetchSkillMainValue(skills, NATIVE_LANGUAGE.name().replace("_", " ")));
  }

  protected void assertThatDodgeIsHalfOfDexterity() {
    assertThat(coreStats.get(MainCharacteristicEnum.DEXTERITY).intValue())
        .isEqualTo(fetchSkillMainValue(skills, DODGE.name()) * 2);
  }

  protected void assertThatCharacteristicsPointsAreEqualTo500() {
    assertThat(
        characteristics.stream().filter(
                keepOnlyMainCharacterics(attributes))
            .map(AttributeVO::getMainValue).reduce(BigDecimal.ZERO, BigDecimal::add)
            .intValue()).isEqualTo(500);
  }

  private Integer fetchSkillMainValue(List<AttributeVO> attributes, String skill) {
    return attributes.stream()
        .filter(
            element -> skill
                .equalsIgnoreCase(element.getAttributeName()))
        .findAny().map(AttributeVO::getMainValue).map(BigDecimal::intValue).orElse(0);
  }

  private Predicate<AttributeVO> keepOnlyMainCharacterics(String attributes) {
    return attributeVO -> attributes.contains(attributeVO.getAttributeName().toUpperCase());
  }


}
