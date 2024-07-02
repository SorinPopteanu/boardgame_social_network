package com.boardgame.boardgame;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BoardgameRepository extends JpaRepository<Boardgame, Integer>, JpaSpecificationExecutor<Boardgame> {

    @Query("""
            SELECT boardgame
            FROM Boardgame boardgame
            WHERE boardgame.archived = false
            AND boardgame.shareable = true
            AND boardgame.owner.id != :userId
            """)
    Page<Boardgame> findAllDisplayableBoardgames(Pageable pageable, Integer id);
}
