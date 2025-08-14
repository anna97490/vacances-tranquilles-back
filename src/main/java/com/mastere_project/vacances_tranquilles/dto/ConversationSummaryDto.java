package com.mastere_project.vacances_tranquilles.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConversationSummaryDto(
        Long conversationId,
        String otherUserName,
        String serviceTitle,
        LocalDate reservationDate,
        LocalTime startTime
) {}