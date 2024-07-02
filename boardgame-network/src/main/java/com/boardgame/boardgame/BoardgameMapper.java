package com.boardgame.boardgame;

import com.boardgame.file.FileUtils;
import com.boardgame.history.BoardgameTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BoardgameMapper {

    public Boardgame toBoardgame(BoardgameRequest boardgameRequest) {
        return Boardgame.builder()
                .id(boardgameRequest.id())
                .name(boardgameRequest.name())
                .author(boardgameRequest.author())
                .description(boardgameRequest.description())
                .boardgameCover(boardgameRequest.boardgameCover())
                .shareable(boardgameRequest.shareable())
                .archived(false)
                .build();
    }

    public BoardgameResponse toBoardgameResponse(Boardgame boardgame) {
        return BoardgameResponse.builder()
                .id(boardgame.getId())
                .name(boardgame.getName())
                .author(boardgame.getAuthor())
                .description(boardgame.getDescription())
                .owner(boardgame.getOwner().getFullName())
                .rate(boardgame.getRate())
                .shareable(boardgame.isShareable())
                .archived(boardgame.isArchived())
                .cover(FileUtils.readFileFromLocation(boardgame.getBoardgameCover()))
                .build();
    }

    public BorrowedBoardgamesResponse toBorrowedBoardgamesResponse(BoardgameTransactionHistory history) {
        return BorrowedBoardgamesResponse.builder()
                .id(history.getBoardgame().getId())
                .name(history.getBoardgame().getName())
                .author(history.getBoardgame().getAuthor())
                .description(history.getBoardgame().getDescription())
                .rate(history.getBoardgame().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturned())
                .build();
    }

}
