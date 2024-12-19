package com.example.side.project_event.planner.Controller;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.EventPricing;
import com.example.side.project_event.planner.Model.Reservation;
import com.example.side.project_event.planner.Model.User;
import com.example.side.project_event.planner.repository.EventRepository;
import com.example.side.project_event.planner.services.EventService;
import com.example.side.project_event.planner.services.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import org.springframework.http.HttpStatus;

@Controller
public class EventController {

    EventService eventService;
    AuthController authController;
    ReservationService reservationService;

    public EventController(EventRepository eventRepository, AuthController authController, ReservationService reservationService) {
        this.eventService = new EventService(eventRepository);
        this.authController = authController;
        this.reservationService = reservationService;
    }

    @GetMapping("/createEvent")
    private String showHostEvent() {
        return "createEvent";
    }

    @GetMapping("/event_details/{id}")
    private String showEventDetails(@PathVariable int id, Model model) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            model.addAttribute("event", event);
            //model.addAttribute("isReservationRequired", event.getIsReservationRequired()); // Pass reservation status
            return "event_details";
        }
        return "errorPage"; // Redirect if event not found
    }

    @GetMapping("/reserve/{id}")
    public String showReservation(@PathVariable int id, Model model, HttpSession session) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            model.addAttribute("event", event);
            session.setAttribute("event", event);  // Add this line to set the event in the session

            User user = event.getCreatedBy();
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
        if (session.getAttribute("currentUser") == null) {
            // User is not logged in; redirect to login with the target URL as a parameter
            return "redirect:/login?redirectTo=/reserve/" + id;
        }

        return "reservation";
    }



    @PostMapping("/perform_creating_event")
    public String performCreatingEvent(@RequestParam("eventName") String name,
                                       @RequestParam("category") String category,
                                       @RequestParam("date") String date,
                                       @RequestParam("location") String location,
                                       @RequestParam("duration") String duration,
                                       @RequestParam("description") String description,
                                       @RequestParam("capacity") int capacity,
                                       @RequestParam(value = "isFree", required = false) EventPricing eventPricing,
                                       @RequestParam(value = "isReservationRequired", required = false) Boolean isReservationRequired,
                                       @RequestParam(value = "pricePerPerson", required = false) Double pricePerPerson, HttpSession session) {

        User currentUser = (User) session.getAttribute("currentUser");
        //System.out.println("got the value from post: "+ isReservationRequired);
        if (currentUser != null) {
            Event created = eventService.createEvent(name, category, date, location, duration, description, capacity, eventPricing, isReservationRequired, pricePerPerson, currentUser);
            if (created != null) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/login";
            }
        }
        return "errorPage";
    }

    @PostMapping("/perform_event_details")
    public String performEventDetails() {
        return null;
    }

    @PostMapping("/generate-description")
    public ResponseEntity<?> generateDescription(@RequestBody Map<String, String> body) {
        try {
            ChatLanguageModel cl = OpenAiChatModel.withApiKey("");
            String keywords = body.get("keywords"); // Retrieve keywords from the request body
            String prompt = "Write an event desciption no longer than 50 words using these keywords: " + keywords;
            String description = cl.generate(prompt); // Generate the description based on the prompt
            return ResponseEntity.ok(Map.of("description", description)); // Send the description back as JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("perform_reservation")
    public String performReservation(@RequestParam(value = "numberOfPeople") int numberOfPeople,
                                     @RequestParam Map<String, String> allParams,
                                     HttpSession httpSession) {
        User currentUser = (User) httpSession.getAttribute("currentUser");
        Event event = (Event) httpSession.getAttribute("event");

        if (currentUser == null) {
            return "redirect:/login"; // Adjust to your login logic
        }

        for (int i = 1; i <= numberOfPeople; i++) {
            String name = allParams.get("name" + i);
            String email = allParams.get("email" + i);
            String phone = allParams.get("phone" + i);
            Reservation reservation;
            if(i>1){
                 reservation = reservationService.createReservation(event, name, email, phone, currentUser, 0);

            }else{
                 reservation = reservationService.createReservation(event, name, email, phone, currentUser, numberOfPeople);

            }
            if (reservation == null) {
                // Handle error (you might want to add error handling)
                return "redirect:/error"; // Redirect to an error page
            }
        }

        return "redirect:/dashboard"; // Redirect after processing all reservations
    }

}
