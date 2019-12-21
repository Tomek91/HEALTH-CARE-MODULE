package pl.com.app.service.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DiseaseUniqueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ADiseaseUnique {

    String message() default "DISEASE NAME UNIQUE VALIDATION ERROR";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
