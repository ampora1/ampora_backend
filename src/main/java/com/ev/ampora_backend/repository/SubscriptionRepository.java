package com.ev.ampora_backend.repository;

import com.ev.ampora_backend.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription,String> {
}
