package com.boardgame.history;

import com.boardgame.boardgame.Boardgame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface BoardgameTransactionHistoryRepository extends JpaRepository<BoardgameTransactionHistory, Integer> {

    @Query("""
            SELECT history
            FROM BoardgameTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<BoardgameTransactionHistory> findAllBorrowedBoardgames(Pageable pageable, Integer userId);

    @Query("""
            SELECT history
            FROM BoardgameTransactionHistory history
            WHERE history.boardgame.owner.id = :userId
            """)
    Page<BoardgameTransactionHistory> findAllReturnedBoardgames(Pageable pageable, Integer userId);

    @Query("""
            SELECT
            (COUNT(*) > 0) AS isBorrowed
            FROM BoardgameTransactionHistory history
            WHERE history.user.id = :userId
            AND history.boardgame.id = :boardgameId
            AND history.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(Integer boardgameId, Integer id);

    @Query("""
            SELECT transaction
            FROM BoardgameTransactionHistory transaction
            WHERE transaction.user.id = :userId
            AND transaction.boardgame.id = :boardgameId
            AND transaction.returned = false
            AND transaction.returnApproved = false
            """)
    Optional<BoardgameTransactionHistory> findByBoardgameIdAndUserId(Integer boardgameId, Integer id);

    @Query("""
            SELECT transaction
            FROM BoardgameTransactionHistory transaction
            WHERE transaction.boardgame.owner.id = :userId
            AND transaction.boardgame.id = :boardgameId
            AND transaction.returned = true
            AND transaction.returnApproved = false
            """)
    Optional<BoardgameTransactionHistory> findByBoardgameIdAndOwnerId(Integer boardgameId, Integer id);
}
