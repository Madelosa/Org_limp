package com.tcc.orgLimp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SenhaValidaValidator
        implements ConstraintValidator<SenhaValida, String> {

    @Override
    public boolean isValid(
            String senha,
            ConstraintValidatorContext context) {

        if (senha == null || senha.isBlank()) {
            return false;
        }

        return senha.length() >= 8
                && senha.matches(".*[A-Z].*")
                && senha.matches(".*[a-z].*")
                && senha.matches(".*[0-9].*");
    }
}