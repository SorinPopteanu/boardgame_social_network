package com.boardgame.boardgame;

import com.boardgame.common.BaseEntity;
import com.boardgame.feedback.Feedback;
import com.boardgame.history.BoardgameTransactionHistory;
import com.boardgame.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Boardgame extends BaseEntity {

    private String name;
    private String author;
    private String description;
    private String boardgameCover;
    private boolean archived;
    private boolean shareable;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "boardgame")
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "boardgame")
    private List<BoardgameTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }

        return this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
    }

}
