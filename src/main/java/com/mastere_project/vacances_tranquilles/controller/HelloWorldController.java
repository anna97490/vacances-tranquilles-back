package com.mastere_project.vacances_tranquilles.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
public class HelloWorldController {
    
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!!";
    }
}
