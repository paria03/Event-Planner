package com.example.side.project_event.planner.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event eventId;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
    //number of people they want to register
    @Column(name = "numberOFPeople")
    private int numberOfPeople;
    //capacity
    @Column(name = "capacity")
    private int capacity;
    //cap-number of people
    @Column(name = "remain")
    private int remain;
    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "registeredBy")
    private int registeredBy;

    public int getRegisteredBy() {
        return registeredBy;
    }

    public Event getEventId() {
        return eventId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRemain() {
        return remain;
    }

    public int getId() {
        return id;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public String getPhone() {
        return phone;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int totalPrice(int pricePerPerson) {
        return numberOfPeople * pricePerPerson;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setEventId(Event eventId) {
        this.eventId = eventId;
    }

    public void setRegisteredBy(int registeredBy) {
        this.registeredBy = registeredBy;
    }
}
