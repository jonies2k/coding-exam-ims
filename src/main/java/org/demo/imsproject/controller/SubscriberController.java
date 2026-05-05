package org.demo.imsproject.controller;

import org.demo.imsproject.dto.SubscriberDTO;
import org.demo.imsproject.entity.Subscriber;
import org.demo.imsproject.service.SubscriberService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/subscriber")
public class SubscriberController {

    private final SubscriberService subsService;

    public SubscriberController(SubscriberService subsService) {
        this.subsService = subsService;
    }

    @GetMapping("/{phoneNumber}")
    public SubscriberDTO getSubscriber(@PathVariable String phoneNumber) {
        return subsService.getSubscriberByPhoneNumber(phoneNumber);
    }

    @DeleteMapping("/{phoneNumber}")
    public String deleteSubscriber(@PathVariable String phoneNumber) {
        subsService.deleteSubscriberByPhoneNumber(phoneNumber);
        return "Subscriber deleted successfully: " + phoneNumber;
    }

    @PutMapping("/{phoneNumber}")
    public ResponseEntity<SubscriberDTO> saveOrUpdateSubscriber( @PathVariable String phoneNumber, @RequestBody SubscriberDTO subscriber) {
        SubscriberDTO savedSubscriber = subsService.saveOrUpdateSubscriber(phoneNumber, subscriber);
        return ResponseEntity.ok(savedSubscriber);
    }
}
