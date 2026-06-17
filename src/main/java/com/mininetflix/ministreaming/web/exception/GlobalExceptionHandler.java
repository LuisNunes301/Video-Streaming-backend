package com.mininetflix.ministreaming.web.exception;

import com.mininetflix.ministreaming.domain.playback.exception.VideoNotFoundException;
import com.mininetflix.ministreaming.domain.user.exception.EmailAlreadyExistsException;
import com.mininetflix.ministreaming.domain.user.exception.InvalidCredentialsException;
import com.mininetflix.ministreaming.web.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {

    return ResponseEntity.status(409)
        .body(new ErrorResponse("EMAIL_ALREADY_EXISTS", ex.getMessage()));
  }

  // @ExceptionHandler(RuntimeException.class)
  // public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {

  // return ResponseEntity
  // .status(400)
  // .body(new ErrorResponse(
  // "BUSINESS_ERROR",
  // ex.getMessage()));
  // }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {

    return ResponseEntity.status(401)
        .body(new ErrorResponse("INVALID_CREDENTIALS", ex.getMessage()));
  }

  @ExceptionHandler(VideoNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleVideoNotFound(VideoNotFoundException ex) {

    return ResponseEntity.status(404).body(new ErrorResponse("VIDEO_NOT_FOUND", ex.getMessage()));
  }
}
