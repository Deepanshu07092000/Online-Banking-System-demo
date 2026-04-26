package com.springBootProject.Util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;


/*
 * @Constraint → Tells Spring this is a custom validation annotation
 * validatedBy → Links this annotation to the validator class
 */
@Constraint(validatedBy = AgeValidator.class)

/*
 * @Target → Where this annotation can be used
 * FIELD → means we can use it on variables (like dateOfBirth)
 */
@Target({ ElementType.FIELD })

/*
 * @Retention → This annotation will be available at runtime
 * (needed for validation to work)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeAbove18 {
	
    String message() default "You must be at least 18 years old";  //  Default error message when validation fails//

    Class<?>[] groups() default {};             // groups → used for advanced validation grouping (not needed now)

    Class<? extends Payload>[] payload() default {}; // payload → used for metadata (rare use cases)

}
