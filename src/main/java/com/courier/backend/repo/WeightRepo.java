package com.courier.backend.repo;

import com.courier.backend.beans.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WeightRepo extends JpaRepository<Weight , Integer> {

    List<Weight> findByOrderByIdAsc();
}
