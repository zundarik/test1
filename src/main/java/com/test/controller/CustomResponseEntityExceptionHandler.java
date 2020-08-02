package com.test.controller;

import com.test.domain.ResponseError;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  protected ResponseEntity<?> handleResponseStatusException(ResponseStatusException exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    throw new ResponseStatusException(exception.getStatus(), exception.getReason());
  }

  // 400
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final List<String> errors = new ArrayList<>();
    for (final FieldError error : exception.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : exception.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    ResponseError responseError = ResponseError.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .time(Instant.now())
        .message(exception.getLocalizedMessage())
        .build();
    return handleExceptionInternal(exception, responseError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(final BindException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final List<String> errors = new ArrayList<String>();
    for (final FieldError error : exception.getBindingResult().getFieldErrors()) {
      errors.add(error.getField() + ": " + error.getDefaultMessage());
    }
    for (final ObjectError error : exception.getBindingResult().getGlobalErrors()) {
      errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
    }
    ResponseError responseError = ResponseError.builder()
        .code(HttpStatus.BAD_REQUEST.value())
        .errors(errors)
        .time(Instant.now())
        .message(exception.getLocalizedMessage())
        .build();
    return handleExceptionInternal(exception, responseError, headers, HttpStatus.BAD_REQUEST, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      final TypeMismatchException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final String error = exception.getValue() + " value for "
        + exception.getPropertyName() + " should be of type " + exception.getRequiredType();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .error(error)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      final MissingServletRequestPartException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final String error = exception.getRequestPartName() + " part is missing";
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .error(error)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      final MissingServletRequestParameterException exception,
      final HttpHeaders headers, final HttpStatus status,
      final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final String error = exception.getParameterName() + " parameter is missing";
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .error(error)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final String error = exception.getName() + " should be of type "
        + Objects.requireNonNull(exception.getRequiredType()).getName();
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .error(error)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @ExceptionHandler(ValidationException.class)
  protected ResponseEntity<ResponseError> handleValidationExceptionExceptions(ValidationException exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .error(exception.getMessage())
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ResponseError> handleConstraintViolationException(ConstraintViolationException exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final List<String> errors = new ArrayList<String>();
    for (final ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      errors.add(violation.getRootBeanClass().getName() + " "
          + violation.getPropertyPath() + ": " + violation.getMessage());
    }
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST.value())
        .body(ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .errors(errors)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  @ExceptionHandler(AccessDeniedException.class)
  protected ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.FORBIDDEN.value())
        .body(ResponseError.builder()
            .code(HttpStatus.FORBIDDEN.value())
            .error(exception.getMessage())
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  // 404
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      final NoHandlerFoundException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final String error = "No handler found for "
        + exception.getHttpMethod() + " " + exception.getRequestURL();
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND.value())
        .body(ResponseError.builder()
            .code(HttpStatus.NOT_FOUND.value())
            .error(error)
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  // 405
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      final HttpRequestMethodNotSupportedException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final StringBuilder error = new StringBuilder();
    error.append(exception.getMethod());
    error.append(" method is not supported for this request. Supported methods are ");
    Objects.requireNonNull(exception.getSupportedHttpMethods()).forEach(t -> error.append(t).append(" "));
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED.value())
        .body(ResponseError.builder()
            .code(HttpStatus.METHOD_NOT_ALLOWED.value())
            .error(error.toString())
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  // 415
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      final HttpMediaTypeNotSupportedException exception,
      final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    final StringBuilder error = new StringBuilder();
    error.append(exception.getContentType());
    error.append(" media type is not supported. Supported media types are ");
    exception.getSupportedMediaTypes().forEach(t -> error.append(t).append(" "));
    return ResponseEntity
        .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
        .body(ResponseError.builder()
            .code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
            .error(error.substring(0, error.length() - 2))
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }

  // 500
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ResponseError> handleOtherExceptions(Exception exception) {
    log.info(exception.getClass().getName());
    log.error("Fail: {}", exception.getMessage(), exception);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(ResponseError.builder()
            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error(exception.getMessage())
            .time(Instant.now())
            .message(exception.getLocalizedMessage())
            .build());
  }
}
