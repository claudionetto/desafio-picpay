package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    public User updateBalance(User user){
        return this.userRepository.save(user);
    }

    public User findById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public boolean validateUser(User user, BigDecimal amount) throws Exception {

        if (user.getUserType() == UserType.MERCHANT){
            throw new Exception("Lojistas não podem realizar transações, somente receber");
        }

        if (user.getBalance().compareTo(amount) <= 0){
            throw new Exception("Saldo insuficiente para o usuário realizar a transação");
        }

        return true;
    }
}
