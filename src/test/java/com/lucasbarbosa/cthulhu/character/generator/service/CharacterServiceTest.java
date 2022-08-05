package com.lucasbarbosa.cthulhu.character.generator.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class CharacterServiceTest extends CharacterServiceTestSupport {


  @Test
  void givenRandomSheetThenValidateGameRules() {
    buildSheetWithoutParams();
    runTest();
    assertThatCharacteristicsPointsAreEqualTo500();
    assertThatDodgeIsHalfOfDexterity();
    assertThatNativeLanguageIsTheSameAsEducation();
    assertThatForeignLanguageIsHalfOfEducation();
  }


}
