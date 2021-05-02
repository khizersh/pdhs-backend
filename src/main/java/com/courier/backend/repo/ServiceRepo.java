package com.courier.backend.repo;

import com.courier.backend.beans.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepo extends JpaRepository<Service , Integer> {

    List<Service> findByOrderByIdAsc();
}