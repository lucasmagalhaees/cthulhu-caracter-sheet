package com.lucasbarbosa.cthulhu.character.generator.service;

import static com.lucasbarbosa.cthulhu.character.generator.model.RecordVO.mountRecord;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.SIZE;
import static com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum.STRENGTH;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.calculateRandomAge;

import com.lucasbarbosa.cthulhu.character.generator.model.AttributeVO;
import com.lucasbarbosa.cthulhu.character.generator.model.PersonVO;
import com.lucasbarbosa.cthulhu.character.generator.model.RecordVO;
import com.lucasbarbosa.cthulhu.character.generator.model.enums.MainCharacteristicEnum;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface RecordService {

  default RecordVO buildRecord(List<AttributeVO> characterists, List<AttributeVO> skills,
      String nativeLanguageRegion, String foreignLanguageRegion,
      PersonVO creditRating) {
    Map<MainCharacteristicEnum, BigDecimal> coreStats = buildCoreStats(
        characterists);

    RecordVO recordVO = mountRecord(characterists, skills);
    evaluateHitPoints(coreStats, recordVO);
    evaluateMagicPoints(coreStats, recordVO);

    assignBonusDamage(recordVO, coreStats.get(SIZE).add(coreStats.get(STRENGTH)));

    evaluateMovementRate(recordVO, coreStats);

    recordVO.setAge(calculateRandomAge());
    recordVO.setCreditRating(creditRating);

    assignCharacterLanguages(recordVO, nativeLanguageRegion, foreignLanguageRegion, coreStats);

    return recordVO;
  }

  void evaluateHitPoints(Map<MainCharacteristicEnum, BigDecimal> coreStats,
      RecordVO recordVO);

  void evaluateMagicPoints(Map<MainCharacteristicEnum, BigDecimal> coreStats,
      RecordVO recordVO);

  void assignCharacterLanguages(RecordVO recordVO, String nativeLanguageRegion,
      String foreignLanguageRegion,
      Map<MainCharacteristicEnum, BigDecimal> coreStats);

  void assignBonusDamage(RecordVO recordVO, BigDecimal bonusParam);

  Map<MainCharacteristicEnum, BigDecimal> buildCoreStats(
      List<AttributeVO> characterists);

  void evaluateMovementRate(RecordVO recordVO,
      Map<MainCharacteristicEnum, BigDecimal> coreStats);


}
