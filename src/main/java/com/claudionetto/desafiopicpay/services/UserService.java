package com.claudionetto.desafiopicpay.services;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.domain.user.UserType;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.exceptions.InsufficientBalanceException;
import com.claudionetto.desafiopicpay.exceptions.MerchantCannotMakeTransactionsException;
import com.claudionetto.desafiopicpay.exceptions.UserNotFoundException;
import com.claudionetto.desafiopicpay.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> listAll() {
        return this.userRepository.findAll();
    }

    public User save(UserCreateDTO userCreateDTO) {

        var user = new User(userCreateDTO);
        return this.userRepository.save(user);
    }

    public User updateBalance(User user) {
        return this.userRepository.save(user);
    }

    public User findById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
    }

    public boolean validateUser(User user, BigDecimal amount){

        if (user.getUserType() == UserType.MERCHANT) {
            throw new MerchantCannotMakeTransactionsException("Lojistas não podem realizar transações, somente receber");
        }

        if (user.getBalance().compareTo(amount) <= 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para o usuário realizar a transação");
        }

        return true;
    }
}
