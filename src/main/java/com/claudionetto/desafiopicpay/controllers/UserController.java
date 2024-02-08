package com.claudionetto.desafiopicpay.controllers;

import com.claudionetto.desafiopicpay.domain.user.User;
import com.claudionetto.desafiopicpay.dto.UserCreateDTO;
import com.claudionetto.desafiopicpay.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping
    public ResponseEntity<List<User>> listAll(){
        return ResponseEntity.ok(this.userService.listAll());
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody UserCreateDTO userCreateDTO){
        return  new ResponseEntity<>(userService.save(userCreateDTO), HttpStatus.CREATED);
    }

}
