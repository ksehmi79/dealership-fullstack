package com.autovibe.dealership;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

    AppUser user = userService.registerUser(request);

    UserResponse response = new UserResponse(user.getId(), user.getUsername(), user.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
