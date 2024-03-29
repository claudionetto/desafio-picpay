package com.claudionetto.desafiopicpay.controllers;

import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.dto.UserResponseDTO;
import com.claudionetto.desafiopicpay.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listAll(){
        return ResponseEntity.ok(this.userService.listAll());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody UserCreateDTO userCreateDTO){
        return  new ResponseEntity<>(userService.save(userCreateDTO), HttpStatus.CREATED);
    }

}
