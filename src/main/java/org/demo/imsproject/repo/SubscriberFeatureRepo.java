package org.demo.imsproject.repo;

import org.demo.imsproject.entity.Subscriber;
import org.demo.imsproject.entity.SubscriberFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberFeatureRepo extends JpaRepository<SubscriberFeature, String> {

}
