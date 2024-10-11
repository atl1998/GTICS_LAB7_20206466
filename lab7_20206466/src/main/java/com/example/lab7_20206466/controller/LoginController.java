package com.example.lab7_20206466.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/openLoginWindow")
    public String loginWindow(){
        return "login";
    }


    @GetMapping("/register")
    public String register(){
        return "register";
    }
}

