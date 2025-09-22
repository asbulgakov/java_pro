package ru.bulgakov.service;

import ru.bulgakov.dto.LimitDtoRq;
import ru.bulgakov.dto.LimitDtoRs;
import ru.bulgakov.dto.LimitUpdateDtoRq;

public interface LimitService {
    LimitDtoRs getOrCreateUserLimit(Long userId);
    LimitDtoRs checkAndReductionLimit(LimitDtoRq limitDtoRq);
    LimitDtoRs refundLimit(LimitDtoRq limitDtoRq);
    LimitDtoRs updateDailyLimit(Long userId, LimitUpdateDtoRq limitUpdateDtoRq);
    void resetAllLimits();
}
