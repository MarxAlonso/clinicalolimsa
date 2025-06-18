package com.example.clinicalolimsa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class InicioController {


    @GetMapping({"", "/"})
    public String home(Model model) {
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/contacto")
    public String contact(){
        return "contacto";
    }
    @GetMapping("/nosotros")
    public String nosotros(){
        return "nosotros";
    }
}

