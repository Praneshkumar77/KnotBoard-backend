package com.knotboard.repository;

import com.knotboard.entity.StickyNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StickyNoteRepository extends JpaRepository<StickyNote, Long> {

    List<StickyNote> findByBoardId(Long boardId);

}
