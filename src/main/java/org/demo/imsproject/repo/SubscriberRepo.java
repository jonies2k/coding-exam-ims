package org.demo.imsproject.repo;

import org.demo.imsproject.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepo extends JpaRepository<Subscriber, String> {

    Subscriber getSubscriberByPhoneNumber(String phoneNumber);

    /*@Query("SELECT e FROM Subscriber e WHERE e.phoneNumber = :phoneNumber")
    Subscriber findByName(@Param("phoneNumber") String phoneNumber);*/

    void deleteSubscriberByPhoneNumber(String phoneNumber);
}
