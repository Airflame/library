package com.library.exceptions;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path.Node;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Class for handling constraint exceptions thrown in the application.
 *
 *
 */
@ControllerAdvice
public class ConstraintExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles constraint violation exceptions and returns a JSON object containing timestamp, HTTP status code and
     * error message.
     *
     * @param ex Exception thrown upon violating constraint rules in a given entity
     * @return Object containing information about the error
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationExceptions(ConstraintViolationException ex) {
        String message = createMessageFromViolations(ex.getConstraintViolations());
        ValidationErrorResponse validationErrorResponse = ValidationErrorResponse.builder()
                .timestamp(LocalDateTime.now()).status(400).error("Bad Request")
                .message(message).build();
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns a message created from data provided in the set of constraint violations. Each one of the constructed
     * sentences directly corresponds to one element from the set and takes the form of the name of the field,
     * converted from camel case to human readable form, and the violation message concatenated together.
     *
     * @param violations Set of constraint violations
     * @return Constructed message describing provided violations
     */
    public String createMessageFromViolations(Set<ConstraintViolation<?>> violations) {
        StringBuilder message = new StringBuilder();
        violations.forEach(violation -> {
            String field = "";
            for (Node node : violation.getPropertyPath())
                field = node.getName();
            field = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(field), ' ').toLowerCase();
            field = field.substring(0, 1).toUpperCase() + field.substring(1);
            message.append(field).append(" ").append(violation.getMessage()).append(". ");
        });
        return message.substring(0, message.length() - 2);
    }
}
