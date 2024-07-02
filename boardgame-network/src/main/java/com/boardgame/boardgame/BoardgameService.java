package com.boardgame.boardgame;

import com.boardgame.common.PageResponse;
import com.boardgame.exception.OperationNotPermittedException;
import com.boardgame.file.FileStorageService;
import com.boardgame.history.BoardgameTransactionHistory;
import com.boardgame.history.BoardgameTransactionHistoryRepository;
import com.boardgame.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardgameService {

    private final BoardgameMapper boardgameMapper;
    private final BoardgameRepository boardgameRepository;
    private final BoardgameTransactionHistoryRepository boardgameTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BoardgameRequest boardgameRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Boardgame boardgame = boardgameMapper.toBoardgame(boardgameRequest);
        boardgame.setOwner(user);
        return boardgameRepository.save(boardgame).getId();
    }

    public BoardgameResponse findBoardgameById(Integer boardgameId) {
        return boardgameRepository.findById(boardgameId)
                .map(boardgameMapper::toBoardgameResponse)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
    }


    //Using JpaRepository to get all boardgames
    public PageResponse<BoardgameResponse> findAllBoardgames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Boardgame> boardgames = boardgameRepository.findAllDisplayableBoardgames(pageable, user.getId());
        List<BoardgameResponse> boardgameResponse = boardgames.stream()
                .map(boardgameMapper::toBoardgameResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                boardgameResponse,
                boardgames.getNumber(),
                boardgames.getSize(),
                boardgames.getTotalElements(),
                boardgames.getTotalPages(),
                boardgames.isFirst(),
                boardgames.isLast()
        );
    }

    // Using JpaSpecificationExecutor to get all boardgames by owner id
    public PageResponse<BoardgameResponse> findAllBoardgamesByOwner(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Boardgame> boardgames = boardgameRepository.findAll(BoardgameSpecification.withOwnerId(user.getId()), pageable);
        List<BoardgameResponse> boardgameResponse = boardgames.stream()
                .map(boardgameMapper::toBoardgameResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                boardgameResponse,
                boardgames.getNumber(),
                boardgames.getSize(),
                boardgames.getTotalElements(),
                boardgames.getTotalPages(),
                boardgames.isFirst(),
                boardgames.isLast()
        );
    }

    public PageResponse<BorrowedBoardgamesResponse> findAllBorrowedBoardgames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BoardgameTransactionHistory> allBorrowedBoardgames = boardgameTransactionHistoryRepository.findAllBorrowedBoardgames(pageable, user.getId());
        List<BorrowedBoardgamesResponse> boardgamesResponse = allBorrowedBoardgames.stream()
                .map(boardgameMapper::toBorrowedBoardgamesResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                boardgamesResponse,
                allBorrowedBoardgames.getNumber(),
                allBorrowedBoardgames.getSize(),
                allBorrowedBoardgames.getTotalElements(),
                allBorrowedBoardgames.getTotalPages(),
                allBorrowedBoardgames.isFirst(),
                allBorrowedBoardgames.isLast()
        );

    }

    public PageResponse<BorrowedBoardgamesResponse> findAllReturnedBoardgames(int page, int size, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BoardgameTransactionHistory> allBorrowedBoardgames = boardgameTransactionHistoryRepository.findAllReturnedBoardgames(pageable, user.getId());
        List<BorrowedBoardgamesResponse> boardgamesResponse = allBorrowedBoardgames.stream()
                .map(boardgameMapper::toBorrowedBoardgamesResponse)
                .collect(Collectors.toList());
        return new PageResponse<>(
                boardgamesResponse,
                allBorrowedBoardgames.getNumber(),
                allBorrowedBoardgames.getSize(),
                allBorrowedBoardgames.getTotalElements(),
                allBorrowedBoardgames.getTotalPages(),
                allBorrowedBoardgames.isFirst(),
                allBorrowedBoardgames.isLast()
        );
    }

    public Integer updateShareableStatus(Integer boardgameId, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        User user = ((User) connectedUser.getPrincipal());
        if (!boardgame.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this boardgame");
        }
        boardgame.setShareable(!boardgame.isShareable());
        boardgameRepository.save(boardgame);
        return boardgameId;
    }

    public Integer updateArchivedStatus(Integer boardgameId, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        User user = ((User) connectedUser.getPrincipal());
        if (!boardgame.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are not the owner of this boardgame");
        }
        boardgame.setArchived(!boardgame.isArchived());
        boardgameRepository.save(boardgame);
        return boardgameId;
    }

    public Integer borrowBoardgame(Integer boardgameId, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        if (!boardgame.isShareable() || boardgame.isArchived()) {
            throw new OperationNotPermittedException("This boardgame is not shareable or it is archived");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (boardgame.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are the owner of this boardgame");
        }
        final boolean isAlreadyBorrowed = boardgameTransactionHistoryRepository.isAlreadyBorrowedByUser(boardgameId, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("The requested boardgame is already borrowed by someone else");
        }
        BoardgameTransactionHistory boardgameTransactionHistory = BoardgameTransactionHistory.builder()
                .boardgame(boardgame)
                .user(user)
                .returned(false)
                .returnApproved(false)
                .build();
        return boardgameTransactionHistoryRepository.save(boardgameTransactionHistory).getId();
    }

    public Integer returnBoardgame(Integer boardgameId, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        User user = ((User) connectedUser.getPrincipal());
        if (!boardgame.isShareable() || boardgame.isArchived()) {
            throw new OperationNotPermittedException("This boardgame is not shareable or it is archived");
        }
        if (boardgame.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are the owner of this boardgame");
        }
        BoardgameTransactionHistory boardgameTransactionHistory = boardgameTransactionHistoryRepository.findByBoardgameIdAndUserId(boardgameId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("You have not borrowed this boardgame"));
        boardgameTransactionHistory.setReturned(true);
        return boardgameTransactionHistoryRepository.save(boardgameTransactionHistory).getId();
    }

    public Integer approveReturnBoardgame(Integer boardgameId, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        User user = ((User) connectedUser.getPrincipal());
        if (!boardgame.isShareable() || boardgame.isArchived()) {
            throw new OperationNotPermittedException("This boardgame is not shareable or it is archived");
        }
        if (boardgame.getOwner().getId().equals(user.getId())) {
            throw new OperationNotPermittedException("You are the owner of this boardgame");
        }
        BoardgameTransactionHistory boardgameTransactionHistory = boardgameTransactionHistoryRepository.findByBoardgameIdAndOwnerId(boardgameId, user.getId())
                .orElseThrow(() -> new OperationNotPermittedException("The book is not returned yet so you can't approve it"));
        return boardgameTransactionHistoryRepository.save(boardgameTransactionHistory).getId();
    }

    public void uploadBoardgameCoverPicture(MultipartFile file, Authentication connectedUser, Integer boardgameId) {
        Boardgame boardgame = boardgameRepository.findById(boardgameId)
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        User user = ((User) connectedUser.getPrincipal());
        var boardgameCover = fileStorageService.saveFile(file, user.getId());
        boardgame.setBoardgameCover(boardgameCover);
        boardgameRepository.save(boardgame);
    }
}
