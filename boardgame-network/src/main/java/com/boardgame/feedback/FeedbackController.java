package com.boardgame.feedback;

import com.boardgame.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.save(feedbackRequest, connectedUser));
    }

    @GetMapping("/boardgame/{boardgame-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBoardgame(
            @PathVariable("boardgame-id") Integer boardgameId,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBoardgame(boardgameId, page, size, connectedUser));
    }
}
