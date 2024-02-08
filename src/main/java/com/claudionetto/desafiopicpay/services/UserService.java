package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> listAll(){
        return this.userRepository.findAll();
    }

    public User save(UserCreateDTO userCreateDTO) {

        var user = new User(userCreateDTO);

        return this.userRepository.save(user);
    }
}
