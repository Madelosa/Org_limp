
package com.tcc.orgLimp.validation;

import com.tcc.orgLimp.dto.TarefaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PrazoValidoValidator
        implements ConstraintValidator<PrazoValido, TarefaRequest> {

    @Override
    public boolean isValid(
            TarefaRequest request,
            ConstraintValidatorContext context) {

        if (request == null
                || request.getData() == null
                || request.getPrazo() == null) {
            return true;
        }

        return !request.getPrazo().isBefore(request.getData());
    }
}