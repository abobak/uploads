package com.demo.uploads.security;

import com.demo.uploads.model.User;
import com.demo.uploads.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurityHelper {

    private final UsersService usersService;

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return usersService.loadUserByUsername(username);
    }

}
