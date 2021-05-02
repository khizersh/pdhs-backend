package com.courier.backend.repo;

import com.courier.backend.beans.CustomerMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<CustomerMessage , Integer> {
}
