package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.UserResponse;
import com.example.parkingsystem.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@GetMapping("/get")
	public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
	    User user = (User) authentication.getPrincipal(); 
	    UserResponse response = new UserResponse(
	        user.getId(),
	        user.getName(),
	        user.getEmail(),
	        user.getRole()
	    );
	    return ResponseEntity.ok(response);
	}
}
