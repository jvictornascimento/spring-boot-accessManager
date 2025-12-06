package com.jvictornascimento.accessmanager.security.user;

import com.jvictornascimento.accessmanager.repository.UserRepository;
import com.jvictornascimento.accessmanager.service.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jvictornascimento.accessmanager.service.exceptions.BaseMessageError.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AccessManagerUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND.getMassage()));
        return AccessManagerUser.buildAccessManagerUser(user);
    }
}
