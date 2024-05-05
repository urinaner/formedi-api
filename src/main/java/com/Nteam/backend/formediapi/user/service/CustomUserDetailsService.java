package com.Nteam.backend.formediapi.user.service;

import com.Nteam.backend.formediapi.user.dto.CustomUserDetails;
import com.Nteam.backend.formediapi.user.entity.UserEntity;
import com.Nteam.backend.formediapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 사용자 조회
        UserEntity userData = userRepository.findByUsername(username);

        // 사용자 데이터가 null인 경우 UsernameNotFoundException을 던짐
        if (userData == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // UserDetails 객체 생성 및 반환
        return new CustomUserDetails(userData);
    }
}