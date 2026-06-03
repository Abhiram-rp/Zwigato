package com.app.ecom.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.ecom.model.User;
import com.app.ecom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    
    public void addUser(User user){
        userRepository.save(user);
    }


    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> fetchUserById(Long id){
        return userRepository.findById(id);
    }


    public boolean updateUserById(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser ->{
                     existingUser.setFirstName(updatedUser.getFirstName());
                     existingUser.setLastName(updatedUser.getLastName());

                     userRepository.save(existingUser);
                     return true;
                 }). orElse(false);
        

        
    }


}
