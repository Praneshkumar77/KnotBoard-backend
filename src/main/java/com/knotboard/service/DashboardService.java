package com.knotboard.service;

import com.knotboard.dto.DashboardDto;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserService userService;
    private final BoardService boardService;
    private final NoteService noteService;

    public DashboardService(UserService userService, BoardService boardService, NoteService noteService) {
        this.userService = userService;
        this.boardService = boardService;
        this.noteService = noteService;
    }

    public DashboardDto getDashboardStats() {
        return new DashboardDto(
                userService.countUsers(),
                boardService.countBoards(),
                noteService.countNotes()
        );
    }

}
