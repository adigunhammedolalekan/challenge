package com.starlingapp.roundup.services;

import com.starlingapp.roundup.models.RoundAndSaveRequest;
import com.starlingapp.roundup.models.SavingsGoal;

public interface RoundingService {

    SavingsGoal roundAndSave(RoundAndSaveRequest request);
}
