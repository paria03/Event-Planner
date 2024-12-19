package com.example.side.project_event.planner.services;

import com.example.side.project_event.planner.Model.Event;
import com.example.side.project_event.planner.Model.Reservation;
import com.example.side.project_event.planner.Model.User;
import com.example.side.project_event.planner.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    ReservationRepository reservationRepository;
    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public Reservation createReservation(Event event, String name, String email, String phone, User user, int numberOfPeople){
        Reservation reservation=new Reservation();
        reservation.setName(name);
        reservation.setEmail(email);
        reservation.setPhone(phone);
        if(name.equals(user.getUsername())){
            reservation.setUser(user);
        }else{
            reservation.setUser(null);
        }
        reservation.setRegisteredBy(user.getId());

        reservation.setEventId(event);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setRemain(event.getCapacity()-numberOfPeople);
        reservation.setCapacity(event.getCapacity());
        if(event.getPricePerPerson()!=null){
            reservation.setTotalPrice(event.getPricePerPerson()*numberOfPeople);
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> findReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }}
