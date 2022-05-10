package com.lucasbarbosa.cthulhu.character.generator.driver.validation;

import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.convertEnumToStringList;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.convertObjectToString;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.getEnumAssuranceMessage;
import static com.lucasbarbosa.cthulhu.character.generator.driver.util.ApplicationUtils.joinStringListByComma;

import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Lucas Barbosa on 27/06/2021
 */
public class EnumAssuranceValidator implements ConstraintValidator<EnumAssurance, CharSequence> {

  private List<String> allowedValues;
  private String field;

  @Override
  public void initialize(EnumAssurance constraintAnnotation) {
    allowedValues = convertEnumToStringList(constraintAnnotation.enumClass());
    field = constraintAnnotation.field();
  }

  private String createConstraintViolationMessage() {
    return String.format(getEnumAssuranceMessage(), field, joinStringListByComma(allowedValues));
  }

  @Override
  public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
    boolean isValid = allowedValues.contains(convertObjectToString(value).toUpperCase());

    if (!isValid) {
      handleConstraintViolation(context);
    }

    return isValid;
  }

  private void handleConstraintViolation(ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(createConstraintViolationMessage())
        .addConstraintViolation();
  }
}
