package com.example.demo.service;

import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.request.SignUpRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Service
@Transactional
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
        DefaultAssert.isTrue(!userRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber()), "중복 전화번호");

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

    public ResponseEntity<?> refresh(String refreshToken) {
        boolean checkValid = valid(refreshToken);
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        Authentication authentication = jwtService.getAuthenticationByEmail(token.get().getUserEmail());

        TokenDto tokenDto;

        Long expirationTime = jwtService.getExpiration(refreshToken);
        if (expirationTime > 0) {
            tokenDto = jwtService.refreshToken(authentication, refreshToken);
        } else {
            tokenDto = jwtService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenDto.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenDto.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> logout(String refreshToken) {
        boolean checkValid = valid(refreshToken);
        DefaultAssert.isAuthentication(checkValid);
        
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        tokenRepository.delete(token.get());

        return ResponseEntity.ok("로그아웃 성공");
    }

    private boolean valid(String refreshToken) {
        boolean validateCheck = jwtService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "탈퇴 처리된 회원입니다.");

        Authentication authentication = jwtService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }
}