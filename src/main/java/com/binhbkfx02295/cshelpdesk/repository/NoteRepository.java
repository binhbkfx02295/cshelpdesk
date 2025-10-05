package com.binhbkfx02295.cshelpdesk.repository;

import com.binhbkfx02295.cshelpdesk.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer>  {
    List<Note> findAllByTicket_Id(int ticketId);
    void deleteByIdAndTicket_Id(int noteId, int ticketId);
    boolean existsByIdAndTicket_Id(int noteId, int ticketId);
    boolean existsByTicket_Id(int ticketId);
}
