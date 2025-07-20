package com.griddynamics.storeapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = User.TABLE_NAME)
public class User {

  public static final String TABLE_NAME = "users";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String encodedPassword;

  public User(final String email, final String encodedPassword) {
    this.email = email;
    this.encodedPassword = encodedPassword;
  }

}
