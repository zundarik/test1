package com.test.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "emails")
public class Email {

  @Id
  @GeneratedValue
  @Nullable
  private Long id;

  @NotNull
  @javax.validation.constraints.Email
  @Column(nullable = false, unique = true)
  private String name;

  public Email(@NotNull @javax.validation.constraints.Email String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Email{"
        + "id=" + getId()
        + ", name='" + name + "'"
        + "}";
  }

}
