package com.springBootProject.Util.validation;

import java.time.LocalDate;
import java.time.Period;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AgeValidator implements ConstraintValidator<AgeAbove18,LocalDate> {
	
	@Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {

		 //-----If DOB is null → let @NotNull handle it
		 
        if (dob == null) {
            return true;
        }
         //---------Calculate age using Period
         
        int age = Period.between(dob, LocalDate.now()).getYears();
        
        /*
         * Return true → valid
         * Return false → validation fails
         */
        return age >= 18;
    }

}
