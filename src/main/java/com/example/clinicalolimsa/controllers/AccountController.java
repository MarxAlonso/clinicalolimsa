package com.example.clinicalolimsa.controllers;

import com.example.clinicalolimsa.models.AppUser;
import com.example.clinicalolimsa.models.RegisterDto;
import com.example.clinicalolimsa.repositories.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AccountController {
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/register")
    public String register (Model model){
        RegisterDto registerDto = new RegisterDto();
        model.addAttribute(registerDto);
        model.addAttribute("success",false);
        return "register";
    }

    @PostMapping("/register")
    public String register( Model model, @Valid @ModelAttribute RegisterDto registerDto,
                            BindingResult result){
        if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())){
            result.addError(new FieldError("registerDto","confirmPassword",
                    "Password and Confirm Password do not match"));
        }
        AppUser appUser = appUserRepository.findByEmail(registerDto.getEmail());
        if(appUser!=null){
            result.addError( new FieldError("registerDto","email",
                    "El email address is already used"));
        }
        if(result.hasErrors()){       return "register";     }
        try{
            var bCryptEncoder = new BCryptPasswordEncoder();
            AppUser newUser = new AppUser();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setAddress(registerDto.getAddress());
            newUser.setRole("gerente");
            newUser.setCreatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
            appUserRepository.save(newUser);
            model.addAttribute("registerDto",new RegisterDto());
            model.addAttribute("success",true);
        }catch(Exception ex){
            result.addError(new FieldError("registerDto","firstName",
                    ex.getMessage()));      }
        return "register";
    }
}

