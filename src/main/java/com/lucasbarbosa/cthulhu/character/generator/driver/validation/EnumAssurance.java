package com.lucasbarbosa.cthulhu.character.generator.driver.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import org.apache.commons.lang3.StringUtils;
/** @author Lucas Barbosa on 27/06/2021 */
@Constraint(validatedBy = EnumAssuranceValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface EnumAssurance {

  Class<? extends Enum<?>> enumClass();

  String field();

  String message() default StringUtils.EMPTY;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
