package ru.bulgakov.service;

import ru.bulgakov.dto.LimitDtoRq;
import ru.bulgakov.dto.LimitDtoRs;

public interface LimitService {
    LimitDtoRs getOrCreateUserLimit(Long userId);
    LimitDtoRs checkAndReductionLimit(LimitDtoRq limitDtoRq);
    LimitDtoRs refundLimit(LimitDtoRq limitDtoRq);
    LimitDtoRs updateDailyLimit(LimitDtoRq limitDtoRq);
    void resetAllLimits();
}
