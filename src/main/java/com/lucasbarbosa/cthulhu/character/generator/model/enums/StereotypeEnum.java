package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.APPEARANCE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.CONSTITUTION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.DEXTERITY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.EDUCATION;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.INTELLIGENCE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.POWER;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.SIZE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.STRENGTH;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.ACCOUNTING;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.ANTHROPOLOGY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.APPRAISE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.ART_CRAFT;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.CHARM;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.CLIMB;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DISGUISE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.DRIVE_AUTO;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.ELECTRIC_REPAIR;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FAST_TALK;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FIGHTING_BRAWL;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FIREARMS_HANDGUN;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FIREARMS_RIFLE_SHOTGUN;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.FIRST_AID;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.HISTORY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.INTIMIDATE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.JUMP;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.LAW;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.LIBRARY_USE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.LISTEN;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.LOCKSMITH;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.MECHANICAL_REPAIR;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.MEDICINE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NATURAL_WORLD;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.NAVIGATE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.OCCULT;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.PERSUADE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.PILOT;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.PSYCHOLOGY;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.RIDE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.SCIENCE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.SLEIGHT_OF_HAND;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.SPOT_HIDDEN;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.STEALTH;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.SURVIVAL;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.SWIM;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.THROW;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.SkillEnum.TRACK;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StereotypeEnum {

  ADVENTURER(List.of(DEXTERITY, APPEARANCE), List.of(CLIMB, RIDE, SWIM,
      DRIVE_AUTO, FIGHTING_BRAWL, FIRST_AID)),
  BEEFCAKE(List.of(STRENGTH), List.of(FIGHTING_BRAWL, INTIMIDATE, THROW,
      LISTEN, PSYCHOLOGY, CLIMB)),
  BON_VIVANT(List.of(SIZE),
      List.of(APPRAISE, ART_CRAFT, CHARM, FAST_TALK, SPOT_HIDDEN, PSYCHOLOGY)),
  COLD_BLOODED(List.of(INTELLIGENCE), List.of(DISGUISE, STEALTH,
      FIGHTING_BRAWL, FIREARMS_HANDGUN, FIREARMS_RIFLE_SHOTGUN, TRACK)),
  EGGHEAD(List.of(INTELLIGENCE, EDUCATION),
      List.of(ELECTRIC_REPAIR, MECHANICAL_REPAIR, SCIENCE, LIBRARY_USE, ANTHROPOLOGY, APPRAISE)),
  EXPLORER(List.of(DEXTERITY, POWER),
      List.of(SURVIVAL, TRACK, RIDE, PILOT, NATURAL_WORLD, CLIMB)),
  FEMME_FATALE(List.of(APPEARANCE, INTELLIGENCE),
      List.of(CHARM, FAST_TALK, SLEIGHT_OF_HAND, FIREARMS_HANDGUN, PSYCHOLOGY, LISTEN)),
  GREASE_MONKEY(List.of(INTELLIGENCE),
      List.of(MECHANICAL_REPAIR, DRIVE_AUTO, LOCKSMITH, ELECTRIC_REPAIR, THROW, FIGHTING_BRAWL)),
  HARD_BOILED(List.of(CONSTITUTION),
      List.of(FIREARMS_RIFLE_SHOTGUN, FIREARMS_HANDGUN, INTIMIDATE, FIGHTING_BRAWL, LOCKSMITH,
          DRIVE_AUTO)),
  HARLEQUIN(List.of(APPEARANCE),
      List.of(PERSUADE, DISGUISE, CHARM, FAST_TALK, SLEIGHT_OF_HAND, STEALTH)),
  HUNTER(List.of(INTELLIGENCE, CONSTITUTION),
      List.of(NATURAL_WORLD, SURVIVAL, SWIM, TRACK, FIRST_AID, NAVIGATE)),
  MYSTIC(List.of(POWER), List.of(PSYCHOLOGY, OCCULT, HISTORY, SCIENCE, ART_CRAFT, NATURAL_WORLD)),
  OUTSIDER(List.of(INTELLIGENCE, CONSTITUTION),
      List.of(NATURAL_WORLD, SURVIVAL, SWIM, TRACK, FIRST_AID, NAVIGATE)),
  ROGUE(List.of(DEXTERITY, APPEARANCE),
      List.of(STEALTH, SLEIGHT_OF_HAND, DISGUISE, SPOT_HIDDEN, APPRAISE, ART_CRAFT)),
  SCHOLAR(List.of(EDUCATION),
      List.of(SCIENCE, LIBRARY_USE, MEDICINE, HISTORY, NATURAL_WORLD, ANTHROPOLOGY)),
  SEEKER(List.of(INTELLIGENCE),
      List.of(SPOT_HIDDEN, LISTEN, OCCULT, LIBRARY_USE, DISGUISE, APPRAISE)),
  SIDEKICK(List.of(DEXTERITY, CONSTITUTION),
      List.of(JUMP, NAVIGATE, STEALTH, TRACK, CLIMB, FIRST_AID)),
  STEADFAST(List.of(CONSTITUTION),
      List.of(LAW, PERSUADE, ACCOUNTING, HISTORY, PSYCHOLOGY, FIRST_AID)),
  THRILL_SEEKER(List.of(DEXTERITY, POWER),
      List.of(SURVIVAL, CLIMB, SWIM, NAVIGATE, PILOT, DRIVE_AUTO)),
  TWO_FISTED(List.of(STRENGTH, SIZE),
      List.of(FIGHTING_BRAWL, DRIVE_AUTO, SWIM, THROW, FIREARMS_HANDGUN, INTIMIDATE));

  private List<MainCharacteristicEnum> chars;
  private List<SkillEnum> skills;

  public static List<SkillEnum> findSkills(String literal) {
    return Arrays.stream(StereotypeEnum.values())
        .filter(stereotype -> stereotype.toString().equalsIgnoreCase(literal))
        .findAny().map(StereotypeEnum::getSkills).orElse(new ArrayList<>());
  }
  public static List<MainCharacteristicEnum> findChars(String literal) {
    return Arrays.stream(StereotypeEnum.values())
        .filter(stereotype -> stereotype.toString().equalsIgnoreCase(literal))
        .findAny().map(StereotypeEnum::getChars).orElse(new ArrayList<>());
  }

}
