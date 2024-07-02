package com.boardgame.boardgame;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardgameResponse {

    private Integer id;
    private String name;
    private String author;
    private String description;
    private String owner;
    private byte[] cover;
    private double rate;
    private boolean shareable;
    private boolean archived;
}
