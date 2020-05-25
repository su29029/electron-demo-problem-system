package com.su29029.server.service;

import com.su29029.server.bean.*;
import com.su29029.server.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Message login(User user){

        User resultUser;
        try {
            resultUser = userDao.findUserByUsername(user.getUsername());
        } catch (Exception e) {
            System.out.println(e);
            return new Message("failed", 500);
        }

        if (resultUser != null) {
            try {
                resultUser = userDao.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
            } catch (Exception e) {
                System.out.println(e);
                return new Message("failed", 500);
            }
            if (resultUser != null) {
                // 登录状态写入 session 并返回
                return new Message("success", 200);
            } else {
                return new Message("failed", 402);
            }
        } else {
            return new Message("failed", 401);
        }
    }

    public Message register(User user){
        User resultUser;
        try {
            resultUser = userDao.findUserByUsername(user.getUsername());
        } catch (Exception e){
            System.out.println(e);
            return new Message("failed", 500);
        }

        if (resultUser != null) {
            return new Message("failed", 400);
        } else {
            try {
                userDao.insertUser(user.getUsername(), user.getPassword());
            } catch (Exception e) {
                System.out.println(e);
                return new Message("failed", 500);
            }
            return new Message("success", 200);
        }
    }

    public Message logout(HttpSession session) {
        System.out.println("logout...");
        if (session.getAttribute("user") != null){
            session.removeAttribute("user");
            return new Message("success", 200);
        } else {
            return new Message("you haven't log yet", 400);
        }
    }

}
