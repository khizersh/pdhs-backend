package com.courier.backend.repo;

import com.courier.backend.beans.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZoneRepo extends JpaRepository<Zone , Integer> {
    List<Zone> findByOrderByIdAsc();
}
