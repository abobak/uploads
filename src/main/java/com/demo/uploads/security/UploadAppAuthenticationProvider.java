package com.demo.uploads.security;

import com.demo.uploads.exception.AccessDeniedException;
import com.demo.uploads.model.User;
import com.demo.uploads.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UploadAppAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth)
            throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
        Optional<User> user = userRepository.findByEmail(username);
        if (!user.isPresent() || !BCrypt.checkpw(password, user.get().getPassword())) {
            throw new AccessDeniedException("Incorrect username or password");
        } else {
            return new UsernamePasswordAuthenticationToken(username, password, null);
        }

    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
