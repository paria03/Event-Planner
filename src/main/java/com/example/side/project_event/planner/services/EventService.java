package com.example.side.project_event.planner.services;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.EventPricing;
import com.example.side.project_event.planner.Model.User;
import com.example.side.project_event.planner.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(String eventName, String category, String date, String location, String duration, String description, int capacity, EventPricing eventPricing, Boolean isReservationRequired, Double pricePerPerson, User currentUser) {
        Event event = new Event();
        event.setName(eventName);
        event.setCategory(category);
        event.setDate(date);
        event.setLocation(location);
        event.setDuration(duration);
        event.setDescription(description);
        event.setCapacity(capacity);
        event.setEventPricing(eventPricing);
        event.setReservationRequired(isReservationRequired != null && isReservationRequired);

        event.setCreatedBy(currentUser);
        if (eventPricing == EventPricing.PAID && pricePerPerson != null) {
            event.setPricePerPerson(pricePerPerson);
        } else {
            event.setPricePerPerson(null);
        }
        return eventRepository.save(event); // Save the event to the database
    }

    public List<Event> getEventsByUser(User user) {
        return eventRepository.findByCreatedBy(user);
    }


    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Integer id) {

        Optional<Event> eventOptional = eventRepository.findById(id);

        return eventOptional.orElse(null);

    }

    public List<Event> getEventsByFilters(String date, String location, EventPricing eventPricing, String keywords) {
        return eventRepository.findEventsByFilters(date, location, eventPricing, keywords);
    }

    public List<Event> getHostedEvents(User user) {
        return eventRepository.findByCreatedBy(user);
    }

    public List<Event> getPastEvents() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Event> pastEvents = new ArrayList<>();
        for (Event event : eventRepository.findAll()) {
            LocalDate eventDate = LocalDate.parse(event.getDate(), formatter);
            if (eventDate.isBefore(currentDate)) {
                pastEvents.add(event);
            }
        }
        return pastEvents;
    }

    public List<Event> getUpcomingEvents() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Event> upcomingEvents = new ArrayList<>();
        for (Event event : eventRepository.findAll()) {
            LocalDate eventDate = LocalDate.parse(event.getDate(), formatter);
            if (eventDate.isAfter(currentDate)) {
                upcomingEvents.add(event);
            }
        }
        return upcomingEvents;
    }

    public boolean deleteEvent(int eventId, User currentUser) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getCreatedBy().getId() == currentUser.getId()) {
                eventRepository.delete(event);
                return true;
            }
        }
        return false;
    }

    public void updateEvent(Event event) {

        eventRepository.save(event);

    }
}