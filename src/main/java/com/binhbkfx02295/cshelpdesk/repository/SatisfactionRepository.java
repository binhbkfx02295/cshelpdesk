package com.binhbkfx02295.cshelpdesk.repository;

import com.binhbkfx02295.cshelpdesk.entity.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionRepository extends JpaRepository<Satisfaction, Integer> {
    boolean existsByCode(String code);
}
