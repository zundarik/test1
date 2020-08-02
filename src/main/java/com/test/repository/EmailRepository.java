package com.test.repository;

import com.test.domain.Email;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {

  Optional<Email> findByNameIgnoreCase(String name);

}
