package pl.com.app.service.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpecializationUniqueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASpecializationUnique {

    String message() default "SPECIALIZATION NAME UNIQUE VALIDATION ERROR";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
