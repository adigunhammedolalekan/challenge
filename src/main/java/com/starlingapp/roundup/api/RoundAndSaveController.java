package com.starlingapp.roundup.api;

import com.starlingapp.roundup.models.RoundAndSaveRequest;
import com.starlingapp.roundup.models.SavingsGoal;
import com.starlingapp.roundup.services.RoundingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("round-save")
public class RoundAndSaveController {

    private final RoundingService roundingService;

    public RoundAndSaveController(RoundingService roundingService) {
        this.roundingService = roundingService;
    }

    @PutMapping("")
    public ResponseEntity<SavingsGoal> roundAndSave(
            @RequestBody() RoundAndSaveRequest request) {
        return ResponseEntity.ok(roundingService.roundAndSave(request));
    }
}
