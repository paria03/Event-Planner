package com.example.side.project_event.planner.Model;

import jakarta.persistence.*;


@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @Column(name = "Category")
    private String category;

    @Column(name = "Date")
    private String date;

    @Column(name = "Description")
    private String description;

    @Column(name = "Duration")
    private String duration;

    @Column(name = "Location")
    private String location;

    @Column(name = "Capacity")
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_pricing")
    private EventPricing eventPricing;

    @Column(name = "price_per_person")
    private Double pricePerPerson;

    @Column(name = "IsReservationRequired")
    private Boolean isReservationRequired;

    public int getId() {
        return id;
    }

    public Boolean isReservationRequired() {
        return isReservationRequired;
    }

    public Double getPricePerPerson() {
        return pricePerPerson;
    }

    public EventPricing getEventPricing() {
        return eventPricing;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getLocation() {
        return location;
    }

    public String getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public void setIsReservationRequired(Boolean reservationRequired) {
//        isReservationRequired = reservationRequired;
//    }

    public void setPricePerPerson(Double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public void setEventPricing(EventPricing eventPricing) {
        this.eventPricing = eventPricing;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setReservationRequired(Boolean reservationRequired) {
        isReservationRequired = reservationRequired;
    }

    public void setName(String name) {
        this.name = name;
    }
}
