package com.courier.backend.repo;

import com.courier.backend.beans.Headlines;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeadlineRepo extends JpaRepository<Headlines , Integer> {
}
