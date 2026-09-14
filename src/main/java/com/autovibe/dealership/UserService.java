package com.autovibe.dealership;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {

    this.appUserRepository = appUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AppUser registerUser(RegisterRequest request) {

    if (appUserRepository.findByUsername(request.getUsername()).isPresent()) {
        throw new UsernameAlreadyExistsException("Username already exists");
    }

    AppUser user =
        new AppUser(
            request.getUsername(), passwordEncoder.encode(request.getPassword()), "SALESPERSON");

    return appUserRepository.save(user);
  }
}
