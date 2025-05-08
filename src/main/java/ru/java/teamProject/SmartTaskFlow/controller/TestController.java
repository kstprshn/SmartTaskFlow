package ru.java.teamProject.SmartTaskFlow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController("/test")
public class TestController {

    @GetMapping()
    public String welcome(){
        return "Hello from Kostya and Google!:)";
    }

    @GetMapping("/user")
    public Principal user(Principal principal){
        System.out.println("user name " + principal.getName());
        return principal;
    }
}
