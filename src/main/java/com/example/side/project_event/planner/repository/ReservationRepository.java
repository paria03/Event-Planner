package com.example.side.project_event.planner.repository;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.Reservation;
import com.example.side.project_event.planner.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);

}
