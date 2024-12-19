package com.example.side.project_event.planner.services;

import com.example.side.project_event.planner.Model.User;
import com.example.side.project_event.planner.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String password, String email, HttpSession session, Model model) {
        User user = new User();
        user.setUsername(username);
        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        User userByUsername = userRepository.findUserByUsername(user.getUsername());
        // User userByEmail=userRepository.findByEmail(email);
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists, please try another one");
//        } if ( userByEmail!=null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists, please try another one");
        } else {
            session.setAttribute("currentUser", user);
            model.addAttribute("currentUser", user);
            userRepository.save(user);
        }
    }

    public int loginUser(String userName, String password, HttpSession session, Model model) {
        User currentUser = findByUsername(userName);
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (currentUser!=null && currentUser.getUsername().equals(user.getUsername()) && passwordEncoder.matches(password, user.getPassword())) {
                session.setAttribute("currentUser", currentUser);
                model.addAttribute("currentUser", currentUser);

                return 1;
            }
        }
        return 0;
    }

    public User findByUsername(String name) {
        return userRepository.findUserByUsername(name);
    }

    public void addGuest(User guest){
        guest.setId(0);
        userRepository.save(guest);
    }
}
