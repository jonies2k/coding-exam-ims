package org.demo.imsproject.dto;


public record SubscriberDTO(
        String phoneNumber,
        String username,
        String password,
        String domain,
        String status,
        FeatureDTO features
) {}
