package com.lucasbarbosa.cthulhu.character.generator.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The behaviour of the API, as the clients see it (the cthulhu-front and the Secret Cultist game). The same rules are
 * the specification of the Go rewrite (cthulhu-char-golang). A sheet is random, so each rule is checked over many.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CharacterApiTest {

  private static final int RUNS = 150;
  private static final List<Integer> CHAR_VALUES = List.of(40, 50, 50, 60, 60, 70, 80, 90);
  private static final List<String> MAIN = List.of("Strength", "Constitution", "Size", "Dexterity", "Appearance",
      "Intelligence", "Power", "Education");

  @Autowired
  private MockMvc mvc;
  private final ObjectMapper json = new ObjectMapper();

  private JsonNode sheet(String query) throws Exception {
    var body = mvc.perform(get("/random-char" + query)).andExpect(status().isOk())
        .andExpect(header().string("Access-Control-Allow-Origin", "*"))
        .andReturn().getResponse().getContentAsString();
    return json.readTree(body);
  }

  private static Map<String, JsonNode> byName(JsonNode s, String first, String second) {
    Map<String, JsonNode> out = new HashMap<>();
    for (var part : List.of(first, second)) {
      s.get(part).forEach(a -> out.put(a.get("attributeName").asText(), a));
    }
    return out;
  }

  private static int v(Map<String, JsonNode> m, String name) {
    return m.get(name).get("mainValue").asInt();
  }

  @Test
  void characteristicsAreTheEightValuesPlusLuckAndSanity() throws Exception {
    for (int k = 0; k < RUNS; k++) {
      var s = sheet("");
      var c = byName(s, "charFirst", "charSecond");
      assertThat(new java.util.TreeSet<>(c.keySet())).isEqualTo(new java.util.TreeSet<>(concat(MAIN, List.of("Luck", "Sanity"))));
      List<Integer> main = new ArrayList<>();
      MAIN.forEach(n -> main.add(v(c, n)));
      main.sort(null);
      assertThat(main).isEqualTo(CHAR_VALUES);
      assertThat(v(c, "Sanity")).isEqualTo(v(c, "Power"));
      assertThat(v(c, "Luck")).isBetween(40, 90).matches(x -> x % 5 == 0);
      assertThat(s.get("charFirst")).hasSize(5);
      assertThat(s.get("charSecond")).hasSize(5);
    }
  }

  @Test
  void everyAttributeCarriesItsHalfAndFifthAndTheListsAreSortedBestFirst() throws Exception {
    for (int k = 0; k < RUNS; k++) {
      var s = sheet("");
      for (var parts : List.of(List.of("charFirst", "charSecond"), List.of("skillFirst", "skillSecond"))) {
        List<Integer> values = new ArrayList<>();
        for (var part : parts) {
          s.get(part).forEach(a -> {
            int m = a.get("mainValue").asInt();
            assertThat(a.get("halfValue").asInt()).isEqualTo(m / 2);
            assertThat(a.get("fifthValue").asInt()).isEqualTo(m / 5);
            values.add(m);
          });
        }
        List<Integer> sorted = new ArrayList<>(values);
        sorted.sort((a, b) -> b - a);
        assertThat(values).isEqualTo(sorted);
        int first = s.get(parts.get(0)).size(), second = s.get(parts.get(1)).size();
        assertThat(first - second).isBetween(0, 1);
      }
    }
  }

  @Test
  void derivedValuesFollowTheRules() throws Exception {
    for (int k = 0; k < RUNS; k++) {
      var s = sheet("");
      var c = byName(s, "charFirst", "charSecond");
      int con = v(c, "Constitution"), siz = v(c, "Size"), str = v(c, "Strength"), dex = v(c, "Dexterity"),
          pow = v(c, "Power"), edu = v(c, "Education");
      assertThat(s.get("hitPoints").asInt()).isEqualTo((con + siz) / 5);
      assertThat(s.get("magicPoints").asInt()).isEqualTo(pow / 5);
      assertThat(s.get("age").asInt()).isBetween(15, 90);
      int move = s.get("movementRate").asInt();
      assertThat(move).isIn(7, 8, 9);
      assertThat(move == 7).isEqualTo(dex < siz && str < siz);
      var bonus = bonus(str + siz);
      assertThat(s.get("bonusDamage").asText()).isEqualTo(bonus.getKey());
      assertThat(s.get("build").asInt()).isEqualTo(bonus.getValue());
      var skills = byName(s, "skillFirst", "skillSecond");
      assertThat(v(skills, "Dodge")).isEqualTo(dex / 2);
      assertThat(v(skills, "Native Language")).isEqualTo(edu);
      assertThat(v(skills, "Foreign Language")).isEqualTo(edu / 2);
      assertThat(s.get("nativeLanguage").get("value").asInt()).isEqualTo(edu);
      assertThat(s.get("foreignLanguage").get("value").asInt()).isEqualTo(edu / 2);
    }
  }

  private static Map.Entry<String, Integer> bonus(int strSiz) {
    if (strSiz <= 64) return Map.entry("-2", -2);
    if (strSiz <= 84) return Map.entry("-1", -1);
    if (strSiz <= 124) return Map.entry("0", 0);
    if (strSiz <= 164) return Map.entry("1d4", 1);
    if (strSiz <= 204) return Map.entry("1d6", 2);
    return Map.entry((strSiz <= 284 ? 2 : strSiz <= 364 ? 3 : strSiz <= 444 ? 4 : 5) + "d6",
        strSiz <= 284 ? 3 : strSiz <= 364 ? 4 : strSiz <= 444 ? 5 : 6);
  }

  @Test
  void skillsStartAtTheirBaseAndCreditRatingMatchesItsBracket() throws Exception {
    Map<String, Integer> base = Map.of("Persuade", 10, "Fast Talk", 5, "Charm", 15, "Psychology", 10,
        "First Aid", 30, "Spot Hidden", 25);
    Map<String, int[]> brackets = Map.of("Poor", new int[]{1, 8}, "Average", new int[]{10, 48},
        "Wealthy", new int[]{50, 88}, "Rich", new int[]{90, 97}, "Super Rich", new int[]{99, 99});
    for (int k = 0; k < RUNS; k++) {
      var s = sheet("");
      var skills = byName(s, "skillFirst", "skillSecond");
      base.forEach((name, b) -> assertThat(v(skills, name)).isGreaterThanOrEqualTo(b));
      assertThat(skills).doesNotContainKeys("Cthulhu Mythos");
      var credit = s.get("creditRating");
      assertThat(credit.get("skillName").asText()).isEqualTo("Credit Rating");
      var range = brackets.get(credit.get("detail").asText());
      assertThat(range).isNotNull();
      assertThat(credit.get("value").asInt()).isBetween(range[0], range[1]);
      assertThat(v(skills, "Credit Rating")).isEqualTo(credit.get("value").asInt());
    }
  }

  @Test
  void aStereotypeGetsTheBestValueInItsCharacteristic() throws Exception {
    for (int k = 0; k < RUNS; k++) {
      var c = byName(sheet("?stereotype=scholar"), "charFirst", "charSecond");
      assertThat(v(c, "Education")).isEqualTo(90);
      var two = byName(sheet("?stereotype=TWO_FISTED"), "charFirst", "charSecond");
      assertThat(Set.of(v(two, "Strength"), v(two, "Size"))).isEqualTo(Set.of(90, 80));
    }
  }

  @Test
  void languagesComeFromTheAskedRegions() throws Exception {
    for (int k = 0; k < RUNS; k++) {
      var s = sheet("?nativeLanguageRegion=EASTERN_EUROPE&foreignLanguageRegion=africa");
      assertThat(s.get("nativeLanguage").get("skillName").asText()).isEqualTo("Native Language");
      assertThat(s.get("nativeLanguage").get("detail").asText()).isIn("Russian", "Romanian", "Serbian", "Czech");
      assertThat(s.get("foreignLanguage").get("skillName").asText()).isEqualTo("Foreign Language");
      assertThat(s.get("foreignLanguage").get("detail").asText()).isIn("Criolo", "Yoruba", "Egyptian");
    }
  }

  @Test
  void healthAnswersWithoutBuildingASheet() throws Exception {
    mvc.perform(get("/health")).andExpect(status().isOk())
        .andExpect(header().string("Access-Control-Allow-Origin", "*"))
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  void theListsTheFrontShows() throws Exception {
    mvc.perform(get("/random-char/stereotype")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(20))
        .andExpect(jsonPath("$[0].name").value("ADVENTURER"))
        .andExpect(jsonPath("$[0].alias").value("Adventurer"))
        .andExpect(jsonPath("$[0].description").value("Based on DEXTERITY, APPEARANCE"));
    mvc.perform(get("/random-char/region/native")).andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(6))
        .andExpect(jsonPath("$[1].name").value("EASTERN_EUROPE"))
        .andExpect(jsonPath("$[1].alias").value("Eastern Europe"))
        .andExpect(jsonPath("$[1].description").value("Ex: RUSSIAN, ROMANIAN, SERBIAN, CZECH"));
    mvc.perform(get("/random-char/region/foreign")).andExpect(status().isOk())
        .andExpect(jsonPath("$[5].name").value("AFRICA"))
        .andExpect(jsonPath("$[5].description").value("Ex: CRIOLO, YORUBA, EGYPTIAN"));
  }

  private static List<String> concat(List<String> a, List<String> b) {
    List<String> out = new ArrayList<>(a);
    out.addAll(b);
    return out;
  }
}
