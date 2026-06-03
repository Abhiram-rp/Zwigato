package com.app.ecom.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.ecom.dto.UserRequest;
import com.app.ecom.dto.UserResponse;
import com.app.ecom.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //creating new user
    @PostMapping("/api/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return new ResponseEntity<>("User added successfully", HttpStatus.CREATED);
    }

    //fetching all users
    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.status(200).body(userService.fetchAllUsers());
    }

    //fetching user by id
    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return userService.fetchUserById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(()->ResponseEntity.status(404).build());
    }

    //updating user by id
    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUserById(@RequestBody UserRequest updatedUserRequest, @PathVariable Long id){
        boolean updated = userService.updateUserById(id, updatedUserRequest);
        if(updated)
            return ResponseEntity.ok("User updated successfully");
        return ResponseEntity.status(404).build();
    }

}
