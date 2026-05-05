package org.demo.imsproject.service;

import jakarta.transaction.Transactional;
import org.demo.imsproject.dto.CallForwardNoReplyDTO;
import org.demo.imsproject.dto.FeatureDTO;
import org.demo.imsproject.dto.SubscriberDTO;
import org.demo.imsproject.entity.SubscriberFeature;
import org.demo.imsproject.entity.Subscriber;
import org.demo.imsproject.repo.SubscriberRepo;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {

    /*TODO:
        1. Create & implement custom exception handlers.
        2. Implement loggers later.
    */

    private final SubscriberRepo subsRepo;

    public SubscriberService(SubscriberRepo subsRepo) {
        this.subsRepo = subsRepo;
    }

    public SubscriberDTO getSubscriberByPhoneNumber(String phoneNumber) {

        return Optional.ofNullable(subsRepo.getSubscriberByPhoneNumber(phoneNumber))
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Phone number not found: " + phoneNumber));
    }

    @Transactional
    public SubscriberDTO saveOrUpdateSubscriber(String phoneNumber, SubscriberDTO requestDTO) {

        Subscriber subscriber = subsRepo.findById(phoneNumber)
                .orElseGet(Subscriber::new);

        subscriber.setPhoneNumber(phoneNumber);
        subscriber.setUsername(requestDTO.username());
        subscriber.setPassword(requestDTO.password());
        subscriber.setDomain(requestDTO.domain());
        subscriber.setStatus(requestDTO.status());

        if (requestDTO.features() != null && requestDTO.features().callForwardNoReply() != null) {

            updateCallForwardNoReplyFeature(
                    subscriber,
                    requestDTO.features().callForwardNoReply()
            );
        }

        Subscriber savedSubscriber = subsRepo.save(subscriber);

        return convertToDTO(savedSubscriber);
    }

    @Transactional
    public void deleteSubscriberByPhoneNumber(String phoneNumber) {
        if (!subsRepo.existsById(phoneNumber)) {
            throw new RuntimeException("Phone number not found: " + phoneNumber);
        }

        subsRepo.deleteSubscriberByPhoneNumber(phoneNumber);
    }


    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        CallForwardNoReplyDTO callForwardNoReplyDTO = null;

        for (SubscriberFeature feature : subscriber.getFeatures()) {
            if ("CALL_FORWARD_NO_REPLY".equals(feature.getFeatureName())) {
                callForwardNoReplyDTO = new CallForwardNoReplyDTO(
                        feature.isProvisioned(),
                        feature.getDestination()
                );
            }
        }

        FeatureDTO featuresDTO = new FeatureDTO(callForwardNoReplyDTO);

        return new SubscriberDTO(
                subscriber.getPhoneNumber(),
                subscriber.getUsername(),
                subscriber.getPassword(),
                subscriber.getDomain(),
                subscriber.getStatus(),
                featuresDTO
        );
    }

    private void updateCallForwardNoReplyFeature(Subscriber subscriber, CallForwardNoReplyDTO callForwardDto) {

        SubscriberFeature feature = null;

        for (SubscriberFeature existingFeature : subscriber.getFeatures()) {
            if ("CALL_FORWARD_NO_REPLY".equals(existingFeature.getFeatureName())) {
                feature = existingFeature;
                break;
            }
        }

        if (feature == null) {
            feature = new SubscriberFeature();
            feature.setFeatureName("CALL_FORWARD_NO_REPLY");
            feature.setSubscriber(subscriber);
            subscriber.getFeatures().add(feature);
        }

        feature.setProvisioned(callForwardDto.provisioned());
        feature.setDestination(callForwardDto.destination());
    }
}
