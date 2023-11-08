package com.springboot.exceptions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MessageKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private LocaleUtils localeUtils;

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Validate input data and then show errors
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        body.put("message", errors);

        logger.error(String.format("Error message: %s", errors));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        logger.error(String.format("Error message: %s", ex.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateItemException.class)
    public ResponseEntity<?> duplicateItemException(
            DuplicateItemException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("message", ex.getMessage());
        logger.error(String.format("Error message: %s", ex.getMessage()));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("message", errors);
        logger.error(String.format("Error message: %s", errors));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        String msg = localeUtils.getMessageByKey(MessageKeys.ACCESS_DENIED, null);
        body.put("message", msg);
        body.put("status", HttpStatus.FORBIDDEN);
        logger.error(String.format("Error message: %s", msg));
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    /**
     * Handle Runtime exception (BadCredentialsException, DisabledException, EmptyResultDataAccessException...)
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        String msg = "";
        if (ex instanceof BadCredentialsException) {//error authenticate
            msg = localeUtils.getMessageByKey(MessageKeys.JWT_USERNAME_PASSWORD_WRONG, null);
        } else if (ex instanceof DisabledException) {
            msg = localeUtils.getMessageByKey(MessageKeys.JWT_USERNAME_DISABLED, null);
        } else if (ex instanceof EmptyResultDataAccessException) {
            msg = localeUtils.getMessageByKey(MessageKeys.EMPTY_RESULT_DATA_ACCESS, null);
        } else {
            msg = ex.getMessage();
        }

        body.put("errorDetail", ex.getMessage());
        body.put("message", msg);
        body.put("status", HttpStatus.BAD_REQUEST);
        logger.error(String.format("Error message: %s", msg));
        logger.error(String.format("Error detail message: %s", ex.getMessage()));
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * handle Exception Internal (JsonParseException, InvalidFormatException, JsonMappingException...)
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatus status, WebRequest request) {
        Map<String, Object> bodyErrs = new HashMap<>();
        String msg = "";
        if (ex.getCause() instanceof JsonParseException
                || ex.getCause() instanceof InvalidFormatException
                || ex.getCause() instanceof JsonMappingException) {
            msg = localeUtils.getMessageByKey(MessageKeys.JSON_PARSE_ERROR, null);
        } else {
            msg = ex.getMessage();
        }

        bodyErrs.put("errorDetail", ex.getMessage());
        bodyErrs.put("message", msg);
        bodyErrs.put("status", status);
        logger.error(String.format("Error message: %s", msg));
        return new ResponseEntity<>(bodyErrs, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


}
