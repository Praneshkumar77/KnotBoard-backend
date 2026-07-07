package com.knotboard.service;

import com.knotboard.dto.NoteDto;
import com.knotboard.entity.AppUser;
import com.knotboard.entity.Board;
import com.knotboard.entity.StickyNote;
import com.knotboard.exception.ResourceNotFoundException;
import com.knotboard.repository.AppUserRepository;
import com.knotboard.repository.BoardRepository;
import com.knotboard.repository.StickyNoteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final StickyNoteRepository stickyNoteRepository;
    private final BoardRepository boardRepository;
    private final AppUserRepository appUserRepository;

    public NoteService(StickyNoteRepository stickyNoteRepository,
                        BoardRepository boardRepository,
                        AppUserRepository appUserRepository) {
        this.stickyNoteRepository = stickyNoteRepository;
        this.boardRepository = boardRepository;
        this.appUserRepository = appUserRepository;
    }

    public NoteDto createNote(NoteDto noteDto) {
        AppUser currentUser = getCurrentUser();

        Board board = boardRepository.findById(noteDto.getBoardId())
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + noteDto.getBoardId()));

        StickyNote note = new StickyNote();
        note.setContent(noteDto.getContent());
        note.setBoard(board);
        note.setCreatedBy(currentUser);

        StickyNote saved = stickyNoteRepository.save(note);
        return mapToDto(saved);
    }

    public List<NoteDto> getNotesByBoard(Long boardId) {
        return stickyNoteRepository.findByBoardId(boardId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) {
        StickyNote note = stickyNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));

        note.setContent(noteDto.getContent());

        StickyNote updated = stickyNoteRepository.save(note);
        return mapToDto(updated);
    }

    public void deleteNote(Long id) {
        StickyNote note = stickyNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        stickyNoteRepository.delete(note);
    }

    public long countNotes() {
        return stickyNoteRepository.count();
    }

    private AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private NoteDto mapToDto(StickyNote note) {
        return new NoteDto(
                note.getId(),
                note.getContent(),
                note.getBoard().getId(),
                note.getCreatedAt(),
                note.getCreatedBy().getUsername()
        );
    }

}
