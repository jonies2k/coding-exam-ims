package org.demo.imsproject.repo;

import org.demo.imsproject.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepo extends JpaRepository<Subscriber, String> {

    Subscriber getSubscriberByPhoneNumber(String phoneNumber);

    void deleteSubscriberByPhoneNumber(String phoneNumber);
}
