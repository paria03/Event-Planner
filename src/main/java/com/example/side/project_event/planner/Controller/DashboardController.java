package com.example.side.project_event.planner.Controller;

import com.example.side.project_event.planner.Model.EventPricing;
import com.example.side.project_event.planner.Model.Reservation;
import com.example.side.project_event.planner.Model.User;
import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.services.ReservationService;
import com.example.side.project_event.planner.services.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class DashboardController {
    private final EventService eventService;
    private final ReservationService reservationService;

    @Autowired
    public DashboardController(EventService eventService, ReservationService reservationService) {
        this.eventService = eventService;
        this.reservationService = reservationService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login"; // Ensure user is logged in
        }

        List<Event> hostedEvents = eventService.getHostedEvents(currentUser);
        List<Reservation> userReservations = reservationService.findReservationsByUser(currentUser);

        hostedEvents.sort(Comparator.comparing(event -> LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        LocalDate currentDate = LocalDate.now();

        List<Event> pastEvents = new ArrayList<>();
        List<Event> upcomingEvents = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Reservation reservation : userReservations) {
            Event event = reservation.getEventId();
            if (event == null) continue;
            try {
                LocalDate date = LocalDate.parse(event.getDate(), formatter);
                if (date.isBefore(currentDate)) {
                    pastEvents.add(event);
                } else {
                    upcomingEvents.add(event);
                }

                if (event.isReservationRequired() == null) {
                    event.setReservationRequired(false);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pastEvents.sort(Comparator.comparing(event -> LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        upcomingEvents.sort(Comparator.comparing(event -> LocalDate.parse(event.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        model.addAttribute("hostedEvents", hostedEvents);
        model.addAttribute("pastEvents", pastEvents);
        model.addAttribute("upcomingEvents", upcomingEvents);
        model.addAttribute("username", currentUser.getUsername());

        return "dashboard";
    }

    // Edit an event
    @PostMapping("/events/edit/{id}")
    public String editEvent(
            @PathVariable("id") int id,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("date") String date,
            @RequestParam("location") String location,
            @RequestParam("duration") String duration,
            @RequestParam("description") String description,
            @RequestParam("capacity") int capacity,
            @RequestParam("eventPricing") EventPricing eventPricing,
            @RequestParam(value = "pricePerPerson", required = false) Double pricePerPerson,
            @RequestParam(value = "isReservationRequired", required = false) Boolean isReservationRequired,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Event event = eventService.getEventById(id);
        if (event == null || event.getCreatedBy().getId() != currentUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this event.");
        }

        // Ensure isReservationRequired is not null
        if (isReservationRequired == null) {
            isReservationRequired = false;
        }

        // If event is free, set pricePerPerson to null
        if (eventPricing == EventPricing.FREE) {
            pricePerPerson = null;
        }

        // Update event details
        event.setName(name);
        event.setCategory(category);
        event.setDate(date);
        event.setLocation(location);
        event.setDuration(duration);
        event.setDescription(description);
        event.setCapacity(capacity);
        event.setEventPricing(eventPricing);
        event.setPricePerPerson(pricePerPerson);
        event.setReservationRequired(isReservationRequired);

        eventService.updateEvent(event);

        return "redirect:/dashboard?updated";
    }

    // Delete an event
    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") int id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Event event = eventService.getEventById(id);
        if (event == null || event.getCreatedBy().getId() != currentUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this event.");
        }

        eventService.deleteEvent(id, currentUser);

        return "redirect:/dashboard?deleted";
    }
}
