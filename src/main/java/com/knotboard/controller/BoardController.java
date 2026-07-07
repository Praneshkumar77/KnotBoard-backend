package com.knotboard.controller;

import com.knotboard.dto.BoardDto;
import com.knotboard.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<BoardDto> createBoard(@Valid @RequestBody BoardDto boardDto) {
        BoardDto created = boardService.createBoard(boardDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<List<BoardDto>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR','CONTRIBUTOR')")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR')")
    public ResponseEntity<BoardDto> updateBoard(@PathVariable Long id, @Valid @RequestBody BoardDto boardDto) {
        return ResponseEntity.ok(boardService.updateBoard(id, boardDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','FACILITATOR')")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }

}
