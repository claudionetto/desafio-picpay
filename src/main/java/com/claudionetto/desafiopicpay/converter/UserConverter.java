package com.claudionetto.desafiopicpay.converter;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserConverter {
    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDocument(),
                user.getUserType(),
                user.getBalance()
        );
    }
    public List<UserResponseDTO> toUserResponseDTOList(List<User> userList){
        return userList.stream().map(this::toUserResponseDTO).toList();
    }
}
