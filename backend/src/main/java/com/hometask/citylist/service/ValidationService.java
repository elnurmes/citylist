package com.hometask.citylist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ValidationException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Elnur Mammadov
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Component
public class ValidationService {

    private final LocalValidatorFactoryBean validatorFactoryBean;

    public <T> void validateOrThrow(T t) throws ValidationException {
        var result = validate(t);
        if (result.getErrorCount() == 0) {
            return;
        }
        var entities = result.getFieldErrors().stream()
            .collect(Collectors.groupingBy(FieldError::getField))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(field -> Optional.ofNullable(field.getDefaultMessage()).orElse(""))
                    .collect(Collectors.joining("; "))
            ));
        throw new ValidationException("Validation failed: " + entities.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")));
    }

    private <T> BindingResult validate(T t) {
        DataBinder binder = new DataBinder(t);
        binder.setValidator(validatorFactoryBean);
        binder.validate();
        return binder.getBindingResult();
    }

    public <T, ID> T validateAndSave(CrudRepository<T, ID> repository, T entity) throws ValidationException {
        validateOrThrow(entity);
        return repository.save(entity);
    }
}
