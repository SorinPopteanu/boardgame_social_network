package com.boardgame.boardgame;

import org.springframework.data.jpa.domain.Specification;

public class BoardgameSpecification {

    public static Specification<Boardgame> withOwnerId(Integer ownerId) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId));
    }
}
