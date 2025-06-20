package com.mastere_project.vacances_tranquilles.dto;

import java.sql.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {

    private Long id;
    private int rating;
    private String comment;
    private Date createdAt;

    private Long authorId;
    private String authorFullName;

    private Long targetId;
    private String targetFullName;

    private Long serviceId;
    private String serviceTitle;

    private Long scheduleId;
}
