package com.test.service.impl;

import static java.lang.String.format;

import com.test.domain.Email;
import com.test.repository.EmailRepository;
import com.test.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final EmailRepository emailRepository;

  @Override
  public Email create(String name) {
    if (emailRepository.findByNameIgnoreCase(name).isPresent()) {
      return null;
    }
    return update(new Email(name.toLowerCase()));
  }

  @Override
  public Email get(String name) {
    return emailRepository.findByNameIgnoreCase(name)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.UNPROCESSABLE_ENTITY,
            format("Email with name '%s' not found.", name)));
  }

  @Override
  public Email update(Email email) {
    return emailRepository.save(email);
  }
}
