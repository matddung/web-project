package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.SignInRequest;
import com.example.demo.dto.SignUpRequest;
import com.example.demo.dto.TokenDto;
import com.example.demo.entity.Token;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtService;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.DefaultAssert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public ResponseEntity<?> signUp(SignUpRequest signUpRequest) {
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "중복 이메일");
        DefaultAssert.isTrue(!userRepository.existsByNickname(signUpRequest.getNickname()), "중복 닉네임");

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .provider("local")
                .providerId("local")
                .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).body("회원가입 성공");
    }

    public ResponseEntity<?> signIn(SignInRequest signInRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = jwtService.createToken(authentication);

        Token token = Token.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .userEmail(tokenDto.getUserEmail())
                .build();

        tokenRepository.save(token);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenDto.getAccessToken()).refreshToken(token.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }
}