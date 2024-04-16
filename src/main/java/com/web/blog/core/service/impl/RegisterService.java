package com.web.blog.core.service.impl;

import com.web.blog.core.domain.dto.RegisterRequest;
import com.web.blog.core.domain.enums.AppUserRole;
import com.web.blog.core.domain.model.Email;
import com.web.blog.core.domain.model.User;
import com.web.blog.core.domain.model.VerificationToken;
import com.web.blog.core.repository.UserRepository;
import com.web.blog.core.repository.VerificationTokenRepository;
import com.web.blog.core.service.impl.mail.MailServiceImpl;
import com.web.blog.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
@Transactional
public class RegisterService {

    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailServiceImpl mailService;

    private final UserRepository userRepository;


    public RegisterService(VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder, MailServiceImpl mailService, UserRepository userRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    public void signup(RegisterRequest registerRequest) {

        if (!userRepository.existsByUsername(registerRequest.getUsername())) {
            User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);
        user.setAppUserRoles(new ArrayList<>(Arrays.asList(AppUserRole.ROLE_USER)));

        userRepository.save(user);

        String token = generateVerificationToken(user);

        Email email = mailService.generateEmail(user, token);

        mailService.sendMail(email);

    } else
        throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setTokenValue(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token){
        VerificationToken verificationToken = verificationTokenRepository.findByTokenValue(token);
        fetchUserAndEnable(verificationToken);
    }

    private void fetchUserAndEnable(VerificationToken verificationToken){
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username);

        user.setEnabled(true);
        userRepository.save(user);
    }
}
