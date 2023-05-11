package com.costly.costly.controller;

import com.costly.costly.response.User;
import com.costly.costly.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/auth/")
public class AuthController {
    @Autowired
    private UserService userService;

    // signup with:
    /*
    {
        "email",
        "password" - then hash it with bcrypt
        "name"
    }
     */
    @PostMapping("/signup/")
    public com.costly.costly.response.User signup(@RequestBody com.costly.costly.request.post.User user) {
        return userService.signup(user);
    }

    // login with:
    /*
    {
        "email",
        "password"
    }
     */

    @PostMapping("/login/")
    public com.costly.costly.response.User login(@RequestBody com.costly.costly.request.post.User user, HttpServletResponse response) {
        return userService.login(user, response);
    }

    @GetMapping("/logout/")
    public void logout() {
        userService.logout();
    }
}

