package com.costly.costly.service;

import com.costly.costly.repository.UserRepository;
import com.costly.costly.response.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final String SECRET_KEY = "mySecretKey";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 day

    public User signup(com.costly.costly.request.post.User user) {
        com.costly.costly.model.User newUser = new com.costly.costly.model.User();

        // validate if email is already in use
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new RuntimeException("Name is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        int strength = 10;
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength);
        newUser.setPasshash(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(newUser);

        // use lambda to map the user to the response
        return transformUserResponse(newUser);
    }

    public User login(com.costly.costly.request.post.User user, HttpServletResponse response) {
        // validate if email is already in use
        if (!userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is not registered");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (user.getPassword().length() < 8) {
            throw new RuntimeException("Password must be at least 8 characters");
        }

        com.costly.costly.model.User dbUser = userRepository.findByEmail(user.getEmail());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(user.getPassword(), dbUser.getPasshash())) {
            throw new RuntimeException("Password is incorrect");
        }

        // TODO: generate JWT token and return it as a response cookie
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        // Set token as a response cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge((int) (EXPIRATION_TIME / 1000));
        cookie.setPath("/");
        response.addCookie(cookie);

        // use lambda to map the user to the response
        return transformUserResponse(dbUser);
    }

    public void logout() {

    }

    private User transformUserResponse(com.costly.costly.model.User dbUser) {
        return userRepository.findById(dbUser.getId()).map(u -> {
            User response = new User();
            response.setId(u.getId());
            response.setName(u.getName());
            response.setEmail(u.getEmail());
            return response;
        }).orElse(null);
    }
}
