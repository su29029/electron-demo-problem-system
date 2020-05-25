package com.su29029.server.controller;

import java.util.List;

import com.su29029.server.bean.Message;
import com.su29029.server.bean.User;
import com.su29029.server.bean.UserSelectedProblemInformation;
import com.su29029.server.service.ProblemService;
import com.su29029.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Main {
    @Autowired
    private UserService userService;
    @Autowired
    private ProblemService problemService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Object login(@RequestBody User user,HttpServletRequest request , HttpSession session) {
        if (request.getSession().getAttribute("user") == user.getUsername()) return new Message("success", 200);
        // if (request.getSession().getAttribute("user").equals(user.getUsername())) return new Message("success", 200);
        if (user.checkInformation().getCode() == -1) return new Message("information invalid", -1);

        switch (userService.login(user).getCode()) {
            case 200:
                session.setAttribute("user", user.getUsername());
                return new Message("success", 200);
            case 401:
                return new Message("username not exist", 401);
            case 402:
                return new Message("password wrong", 402);
            case 500:
                return new Message("something error", 500);
            default:
                return new Message("unknown error", 500);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Message register(@RequestBody User user) {

        if (user.checkInformation().getCode() == -1) return new Message("information invalid", -1);

        switch (userService.register(user).getCode()) {
            case 200:
                return new Message("success", 200);
            case 400:
                return new Message("username has been registered", 400);
            case 500:
                return new Message("something error", 500);
            default:
                return new Message("unknown error", 500);
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Message logout(@RequestBody User user, HttpServletRequest request , HttpSession session){
        return userService.logout(session);
    }

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public Object isLogin(HttpServletRequest request, HttpSession session){
        User user = new User();
        user.setUsername(session.getAttribute("user").toString());
        if (request.getAttribute("loginStatus").equals(1)){
            return new Message("pass", 200, user);
        } else {
            return new Message("login first", 400);
        }
    }

    @RequestMapping(value = "/selectProblem", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Message setProblemOption(@RequestBody UserSelectedProblemInformation userSelectedProblemInformation, HttpServletRequest request, HttpSession session) {
        System.out.println("user:" + userSelectedProblemInformation.getUser().getUsername() + " is going to take it.");
        // problemService.getProblemId();
        // System.out.println(problemService.getProblemId().isEmpty());
        return problemService.prepareProblem(userSelectedProblemInformation);
    }

    @RequestMapping(value = "/getProblem", method = RequestMethod.GET)
    public Object getProblem(@RequestParam int flag){
        
        return problemService.getProblemById(flag);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public User submit(@RequestBody List<String> answer,HttpSession session){
        return problemService.submit(answer,session);
    }
    

}
