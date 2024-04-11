package ua.com.agroswit.productservice.dto.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.com.agroswit.productservice.dto.validation.validator.NameUniqueValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = NameUniqueValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NameUnique {
    String message() default "Name are not unique";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
