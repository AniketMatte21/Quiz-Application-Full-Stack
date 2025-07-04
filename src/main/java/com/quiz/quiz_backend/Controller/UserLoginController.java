package com.quiz.quiz_backend.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/check")
public class UserLoginController
{
    @GetMapping("/isLogin")
    public ResponseEntity<?> isLogin()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth);
        if(auth!=null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String))
        {
            return ResponseEntity.ok(Map.of("isLoggedIn",true
            ,"username",auth.getName()));
        }
        return ResponseEntity.ok(Map.of("isLoggedIn",false));
    }
}
