package com.test.controller;

import com.test.domain.ErrorResponse;
import com.test.service.EmailService;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainController {

  private final EmailService emailService;

  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> email(
      @RequestBody EmailRequest emailRequest) {

    if (emailService.create(emailRequest.getEmail()) != null) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.badRequest().body(new ErrorResponse(true));
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  private static class EmailRequest {

    @NotEmpty
    private String email;
  }
}
