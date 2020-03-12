package com.demo.uploads.service;

import com.demo.uploads.dto.NewUserDto;
import com.demo.uploads.exception.BadRequestException;
import com.demo.uploads.mapper.UserMapper;
import com.demo.uploads.model.User;
import com.demo.uploads.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final Object userCreateOperationsLock = new Object();

    @Transactional
    @Synchronized("userCreateOperationsLock")
    public void createUser(NewUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException(String.format("User with email %s exists", dto.getEmail()));
        }
        User user = userMapper.dtoToNewUser(dto);
        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(s);
    }
}
