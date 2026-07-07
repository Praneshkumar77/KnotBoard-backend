package com.knotboard.controller;

import com.knotboard.dto.NoteDto;
import com.knotboard.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody NoteDto noteDto) {
        NoteDto created = noteService.createNote(noteDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/board/{boardId}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<List<NoteDto>> getNotesByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(noteService.getNotesByBoard(boardId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @Valid @RequestBody NoteDto noteDto) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}
