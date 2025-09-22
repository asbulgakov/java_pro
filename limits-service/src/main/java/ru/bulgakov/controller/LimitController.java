package ru.bulgakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import ru.bulgakov.dto.LimitDtoRq;
import ru.bulgakov.dto.LimitDtoRs;
import ru.bulgakov.dto.LimitUpdateDtoRq;
import ru.bulgakov.service.LimitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/limits")
public class LimitController {
    private final LimitService limitService;

    @GetMapping("/{userId}")
    public LimitDtoRs getLimitByUserId(@PathVariable("userId") long userId) {
        return limitService.getOrCreateUserLimit(userId);
    }

    @PostMapping("/reduction")
    public LimitDtoRs checkAndUpdateLimit(@RequestBody LimitDtoRq limitDtoRq) {
        return limitService.checkAndReductionLimit(limitDtoRq);
    }

    @PostMapping("/refund")
    public LimitDtoRs refundLimit(@RequestBody LimitDtoRq limitDtoRq) {
        return limitService.refundLimit(limitDtoRq);
    }

    @PutMapping("/{userId}/update-limit")
    public LimitDtoRs updateDailyLimit(
            @PathVariable("userId") Long userId,
            @RequestBody LimitUpdateDtoRq limitUpdateDtoRq
    ) {
        return limitService.updateDailyLimit(userId, limitUpdateDtoRq);
    }
}
