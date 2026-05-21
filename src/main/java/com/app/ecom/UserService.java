package com.app.ecom;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private List<User> userList = new ArrayList<>();

    public List<User> addUser(User user){
        userList.add(user);
        return userList;
    }


    public List<User> fetchAllUsers(){
        return userList;
    }


}
