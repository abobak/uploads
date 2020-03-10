package com.demo.uploads.service;

import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.mapper.UserMapper;
import com.demo.uploads.model.User;
import com.demo.uploads.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    public void createUser(NewUserDto dto) {
        User user = userMapper.dtoToNewUser(dto);
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(s);
    }
}
