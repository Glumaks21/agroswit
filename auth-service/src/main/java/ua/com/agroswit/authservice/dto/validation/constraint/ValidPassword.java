package ua.com.agroswit.authservice.dto.validation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ua.com.agroswit.authservice.dto.validation.validator.PasswordConstraintValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {

    String message() default "Invalid Password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
