package com.boardgame.boardgame;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BoardgameRequest(

        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100")
        String name,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String author,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String description,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String boardgameCover,
        @NotNull(message = "104")
        @NotEmpty(message = "104")
        boolean shareable
) {
}
