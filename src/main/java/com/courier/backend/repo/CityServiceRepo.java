package com.courier.backend.repo;

import com.courier.backend.beans.CityServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CityServiceRepo extends JpaRepository<CityServices , Integer> {

    List<CityServices> findByOrderByIdAsc();

    @Query(value = "select * from city_services where service_id=?1 AND zone_id = ?2",nativeQuery = true)
    List<CityServices> findByServiceAndZoneId(Integer sid, Integer cid);

}