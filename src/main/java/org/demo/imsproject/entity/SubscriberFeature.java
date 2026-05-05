package org.demo.imsproject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class SubscriberFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Feature_id")
    private Long featureId;

    private String featureName;

    private boolean provisioned;

    private String destination;

    @Column(name = "Created_dt", nullable = false, updatable = false)
    private LocalDateTime createdDt;

    @Column(name = "Updated_dt", nullable = false)
    private LocalDateTime updatedDt;

    @ManyToOne
    @JoinColumn(name = "phoneNumber", referencedColumnName = "phoneNumber")
    @JsonIgnore
    private Subscriber subscriber;

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

    public SubscriberFeature() {
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public boolean isProvisioned() {
        return provisioned;
    }

    public void setProvisioned(boolean provisioned) {
        this.provisioned = provisioned;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
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
}
