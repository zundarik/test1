package com.test.service;

import com.test.domain.Email;

public interface EmailService {

  Email create(String name);

  Email get(String name);

  Email update(Email email);

}
