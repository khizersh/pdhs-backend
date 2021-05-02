package com.courier.backend.repo;

import com.courier.backend.beans.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepo extends JpaRepository<News ,Integer> {
}
