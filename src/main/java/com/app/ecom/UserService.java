package com.app.ecom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public void addUser(User user){
        user.setId(nextId++);
        userList.add(user);
    }


    public List<User> fetchAllUsers(){
        return userList;
    }

    public Optional<User> fetchUserById(Long id){
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }


    public boolean updateUserById(Long id, User updatedUser) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .map(existingUser ->{
                     existingUser.setFirstName(updatedUser.getFirstName());
                     existingUser.setLastName(updatedUser.getLastName());
                     return true;
                 }). orElse(false);
        

        
    }


}
