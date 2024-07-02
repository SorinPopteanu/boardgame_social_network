package com.boardgame.feedback;

import com.boardgame.boardgame.Boardgame;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .boardgame(Boardgame.builder()
                        .id(feedbackRequest.boardgameId())
                        .archived(false)
                        .shareable(false)
                        .build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer userId) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), userId))
                .build();
    }
}
