package com.boardgame.boardgame;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BorrowedBoardgamesResponse {

    private Integer id;
    private String name;
    private String author;
    private String description;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
