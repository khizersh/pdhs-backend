package com.courier.backend.repo;

import com.courier.backend.beans.Offers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepo extends JpaRepository<Offers , Integer> {
}
