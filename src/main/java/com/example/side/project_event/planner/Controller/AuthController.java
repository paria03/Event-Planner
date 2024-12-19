package com.example.side.project_event.planner.Controller;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.EventPricing;
import com.example.side.project_event.planner.services.EventService;
import com.example.side.project_event.planner.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class AuthController {
    private final EventService eventService;
    UserService userService;

    //this tag is for automatic instantiation
    @Autowired
    public AuthController(UserService userService, EventService eventService) {
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String showHome(@RequestParam(value = "date", required = false) String dateParam,
                           @RequestParam(value = "location", required = false) String locationParam,
                           @RequestParam(value = "eventType", required = false) String eventTypeParam,
                           @RequestParam(value = "keywords", required = false) String keywordsParam,
                           HttpSession session, Model model) {

        // Convert empty strings to null
        String date = (dateParam != null && !dateParam.trim().isEmpty()) ? dateParam : null;
        String location = (locationParam != null && !locationParam.trim().isEmpty()) ? locationParam : null;
        String keywords = (keywordsParam != null && !keywordsParam.trim().isEmpty()) ? keywordsParam : null;

        // Convert eventType to EventPricing enum
        EventPricing eventPricing = null;
        if (eventTypeParam != null && !eventTypeParam.trim().isEmpty()) {
            eventPricing = EventPricing.valueOf(eventTypeParam.toUpperCase());
        }

        // Fetch events with filters
        List<Event> events = eventService.getEventsByFilters(date, location, eventPricing, keywords);
        model.addAttribute("events", events);

        model.addAttribute("events", events);
        if (session.getAttribute("currentUser") != null) {
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String ShowSignup() {
        return "signup";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @PostMapping("/perform_login")
    public String performLogin(@RequestParam("Username") String username, @RequestParam("Password") String password, @RequestParam(value = "redirectTo", required = false) String redirectTo, HttpSession session, Model model) {
        int loginResult = userService.loginUser(username, password, session, model);
        if (loginResult == 1) {
            return redirectTo != null ? "redirect:" + redirectTo : "redirect:/dashboard";
        }
        return "redirect:/login";
    }

    @PostMapping("/perform_signup")
    public String performSignup(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email, HttpSession session, Model model
    ) {
        try {
            userService.registerUser(username, password, email, session, model);
            return "redirect:/dashboard";
        } catch (ResponseStatusException ex) {
            model.addAttribute("errorMessage", ex.getReason());
            return "signup";
        }

    }

}
