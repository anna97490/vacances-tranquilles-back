package com.mastere_project.vacances_tranquilles.dto;

import java.sql.Date;

public class ScheduleDTO {
        
    private Long id;
    private Date date;
    private boolean isBooked;

    private Long serviceId;        // ID de la prestation associée
    private String serviceTitle;   // Titre de la prestation (optionnel)

    private Long userId;           // ID du particulier qui a réservé (nullable si pas encore réservé)
    private String userFullName;   // Nom complet du particulier (optionnel)

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }
}
