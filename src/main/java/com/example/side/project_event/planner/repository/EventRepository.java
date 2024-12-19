package com.example.side.project_event.planner.repository;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.EventPricing;
import com.example.side.project_event.planner.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByCreatedBy(User user);
    Event findEventById(Integer id);

    @Query("SELECT e FROM Event e WHERE "
            + "(:date IS NULL OR :date = '' OR e.date = :date) AND "
            + "(:location IS NULL OR :location = '' OR UPPER(e.location) LIKE CONCAT('%', UPPER(:location), '%')) AND "
            + "(:eventPricing IS NULL OR e.eventPricing = :eventPricing) AND "
            + "(:keywords IS NULL OR :keywords = '' OR UPPER(e.description) LIKE CONCAT('%', UPPER(:keywords), '%'))")
    List<Event> findEventsByFilters(
            @Param("date") String date,
            @Param("location") String location,
            @Param("eventPricing") EventPricing eventPricing,
            @Param("keywords") String keywords);
}
