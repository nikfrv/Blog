package com.web.blog.controller;

import com.web.blog.core.domain.dto.LogOutRequest;
import com.web.blog.core.domain.dto.LoginRequest;
import com.web.blog.core.domain.dto.TokenRefreshRequest;
import com.web.blog.core.service.impl.LoginService;
import com.web.blog.core.service.impl.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    private final RefreshTokenService refreshTokenService;

    public LoginController(LoginService loginService, RefreshTokenService refreshTokenService) {
        this.loginService = loginService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody LoginRequest loginRequest) {
       return ResponseEntity.status(HttpStatus.OK).body(loginService.login(loginRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@Valid @RequestBody TokenRefreshRequest request){
        String requestRefreshToken = request.getRefreshToken();
        return ResponseEntity.status(HttpStatus.OK).body(loginService.refreshToken(requestRefreshToken));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
        refreshTokenService.deleteByUserId(logOutRequest.getUserId());
        return ResponseEntity.ok().body("Success logout!");
    }
}
