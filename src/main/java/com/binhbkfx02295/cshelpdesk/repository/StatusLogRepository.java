package com.binhbkfx02295.cshelpdesk.repository;

import com.binhbkfx02295.cshelpdesk.entity.StatusLog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusLogRepository extends JpaRepository<StatusLog, Integer> {

    @EntityGraph(attributePaths = {"status"})
    Optional<StatusLog> findFirstByEmployee_UsernameOrderByTimestampDesc(String username);


    List<StatusLog> findAllByEmployee_Username(String username);
}
