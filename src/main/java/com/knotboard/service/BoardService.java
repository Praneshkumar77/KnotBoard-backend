package com.knotboard.service;

import com.knotboard.dto.BoardDto;
import com.knotboard.entity.AppUser;
import com.knotboard.entity.Board;
import com.knotboard.exception.ResourceNotFoundException;
import com.knotboard.repository.AppUserRepository;
import com.knotboard.repository.BoardRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final AppUserRepository appUserRepository;

    public BoardService(BoardRepository boardRepository, AppUserRepository appUserRepository) {
        this.boardRepository = boardRepository;
        this.appUserRepository = appUserRepository;
    }

    public BoardDto createBoard(BoardDto boardDto) {
        AppUser currentUser = getCurrentUser();

        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setDescription(boardDto.getDescription());
        board.setCreatedBy(currentUser);

        Board saved = boardRepository.save(board);
        return mapToDto(saved);
    }

    public List<BoardDto> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    public BoardDto getBoardById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        return mapToDto(board);
    }

    public BoardDto updateBoard(Long id, BoardDto boardDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));

        board.setTitle(boardDto.getTitle());
        board.setDescription(boardDto.getDescription());

        Board updated = boardRepository.save(board);
        return mapToDto(updated);
    }

    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        boardRepository.delete(board);
    }

    public long countBoards() {
        return boardRepository.count();
    }

    private AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    private BoardDto mapToDto(Board board) {
        return new BoardDto(
                board.getId(),
                board.getTitle(),
                board.getDescription(),
                board.getCreatedAt(),
                board.getCreatedBy().getUsername()
        );
    }

}
