package com.boardgame.boardgame;

import com.boardgame.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/boardgames")
@RequiredArgsConstructor
@Tag(name = "Boardgame")
public class BoardgameController {

    private final BoardgameService boardgameService;

    @PostMapping
    public ResponseEntity<Integer> saveBoardgame(
            @Valid @RequestBody BoardgameRequest boardgameRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.save(boardgameRequest, connectedUser));
    }

    @GetMapping("{boardgame-id}")
    public ResponseEntity<BoardgameResponse> findBoardgameById(
            @PathVariable("boardgame-id") Integer boardgameId
    ) {
        return ResponseEntity.ok(boardgameService.findBoardgameById(boardgameId));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BoardgameResponse>> findAllBoardgames(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.findAllBoardgames(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BoardgameResponse>> findAllBoardgamesByOwner(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.findAllBoardgamesByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBoardgamesResponse>> findAllBorrowedBoardgames(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.findAllBorrowedBoardgames(page, size, connectedUser));
    }

    @GetMapping("returned")
    public ResponseEntity<PageResponse<BorrowedBoardgamesResponse>> findAllReturnedBoardgames(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.findAllReturnedBoardgames(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{boardgame-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("boardgame-id") Integer boardgameId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.updateShareableStatus(boardgameId, connectedUser));
    }

    @PatchMapping("/archived/{boardgame-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("boardgame-id") Integer boardgameId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.updateArchivedStatus(boardgameId, connectedUser));
    }

    @PostMapping("/borrow/{boardgame-id}")
    public ResponseEntity<Integer> borrowBoardgame(
            @PathVariable("boardgame-id") Integer boardgameId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.borrowBoardgame(boardgameId, connectedUser));
    }

    @PatchMapping("/borrow/return/{boardgame-id}")
    public ResponseEntity<Integer> returnBoardgame(
            @PathVariable("boardgame-id") Integer boardgameId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.returnBoardgame(boardgameId, connectedUser));
    }

    @PatchMapping("/borrow/return/approve/{boardgame-id}")
    public ResponseEntity<Integer> approveReturnBoardgame(
            @PathVariable("boardgame-id") Integer boardgameId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(boardgameService.approveReturnBoardgame(boardgameId, connectedUser));
    }

    @PostMapping(value = "/cover/{boardgame-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBoardgameCoverPicture(
            @PathVariable("boardgame-id") Integer boardgameId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        boardgameService.uploadBoardgameCoverPicture(file, connectedUser, boardgameId);
        return ResponseEntity.accepted().build();
    }



}
