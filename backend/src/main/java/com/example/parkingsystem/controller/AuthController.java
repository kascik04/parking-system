package com.example.parkingsystem.controller;

import com.example.parkingsystem.dto.RegisterRequest;
import com.example.parkingsystem.dto.AuthenticationRequest;
import com.example.parkingsystem.dto.AuthenticationResponse;
import com.example.parkingsystem.entity.User;
import com.example.parkingsystem.repository.UserRepository;
import com.example.parkingsystem.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    // private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng.");
        }

        User user = new User(null, request.getName(), request.getEmail(), request.getPassword(), "USER");
        userRepository.save(user);
        return ResponseEntity.ok("Đăng ký thành công.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        var userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Email không tồn tại.");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Mật khẩu không đúng.");
        }

        String jwtToken = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
    }
}
