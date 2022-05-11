package com.lucasbarbosa.cthulhu.character.generator.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SkillEnum {

    ACCOUNTING(5),
    ANTHROPOLOGY(1),
    APPRAISE(5),
    ARCHAEOLOGY(1),
    ART_CRAFT(5),
    CHARM(15),
    CLIMB(20),
    CREDIT_RATING(0),
    CTHULHU_MYTHOS(0),
    DISGUISE(5),
    DODGE(0),
    DRIVE_AUTO(20),
    ELECTRIC_REPAIR(10),
    FAST_TALK(5),
    FIGHTING_BRAWL(25),
    FIREARMS_HANDGUN(20),
    FIREARMS_RIFLE_SHOTGUN(25),
    FIRST_AID(30),
    HISTORY(5),
    INTIMIDATE(15),
    JUMP(20),
    FOREIGN_LANGUAGE(0),
    NATIVE_LANGUAGE(0),
    LAW(5),
    LIBRARY_USE(20),
    LISTEN(20),
    LOCKSMITH(1),
    MECHANICAL_REPAIR(10),
    MEDICINE(1),
    NATURAL_WORLD(10),
    NAVIGATE(10),
    OCCULT(5),
    PERSUADE(10),
    PILOT(1),
    PSYCHOANALYSIS(1),
    PSYCHOLOGY(10),
    RIDE(5),
    SCIENCE(1),
    SLEIGHT_OF_HAND(10),
    SPOT_HIDDEN(25),
    STEALTH(20),
    SURVIVAL(10),
    SWIM(20),
    THROW(10),
    TRACK(10);


    private Integer initialValue;

}
