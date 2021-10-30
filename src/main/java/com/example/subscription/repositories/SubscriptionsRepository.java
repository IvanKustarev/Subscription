package com.example.subscription.repositories;

import com.example.subscription.models.Subscriptions;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionsRepository extends CrudRepository<Subscriptions, Long> {
}
