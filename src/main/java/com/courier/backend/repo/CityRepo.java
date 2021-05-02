package com.courier.backend.repo;

import com.courier.backend.beans.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityRepo extends JpaRepository <Cities , Integer> {

    List<Cities> findByZoneId(Integer id);
}
