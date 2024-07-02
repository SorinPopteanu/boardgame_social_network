package com.boardgame.feedback;

import com.boardgame.boardgame.Boardgame;
import com.boardgame.boardgame.BoardgameRepository;
import com.boardgame.common.PageResponse;
import com.boardgame.exception.OperationNotPermittedException;
import com.boardgame.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final BoardgameRepository boardgameRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedbackRepository feedbackRepository;

    public Integer save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Boardgame boardgame = boardgameRepository.findById(feedbackRequest.boardgameId())
                .orElseThrow(() -> new EntityNotFoundException("Boardgame not found"));
        if (boardgame.isArchived() || !boardgame.isShareable()) {
            throw new OperationNotPermittedException("No feedback allowed for an archived or unshareable boardgame");
        }
        User user = (User) connectedUser.getPrincipal();
        if (Objects.equals(boardgame.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("No feedback allowed for the owner of the boardgame");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBoardgame(Integer boardgameId, Integer page, Integer size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBoardgameId(boardgameId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId()))
                .toList();

        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
