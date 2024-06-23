package com.dpx.project.yuda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDetails {

    String description;

    String title;

    Long viewCount;

    Long likeCount;

    Long commentCount;

    LocalDateTime publicationDate;
}
