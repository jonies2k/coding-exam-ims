package org.demo.imsproject.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Subscriber {

    @Id
    private String phoneNumber;

    private String username;

    private String password;

    private String domain;

    private String status;

    @Column(name = "Created_dt", nullable = false, updatable = false)
    private LocalDateTime createdDt;

    @Column(name = "Updated_dt", nullable = false)
    private LocalDateTime updatedDt;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubscriberFeature> features = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        LocalDateTime currentDt = LocalDateTime.now();

        if (this.createdDt == null) {
            this.createdDt = currentDt;
        }

        if (this.updatedDt == null) {
            this.updatedDt = currentDt;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDt = LocalDateTime.now();
    }

    public Subscriber() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(LocalDateTime createdDt) {
        this.createdDt = createdDt;
    }

    public LocalDateTime getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(LocalDateTime updatedDt) {
        this.updatedDt = updatedDt;
    }

    public List<SubscriberFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<SubscriberFeature> features) {
        this.features = features;
    }
}
