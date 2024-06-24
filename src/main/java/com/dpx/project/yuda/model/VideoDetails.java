package com.dpx.project.yuda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Model Class for VideoDetails.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDetails {

    String id;

    String description;

    String title;

    Long viewCount;

    Long likeCount;

    Long commentCount;

    LocalDateTime publicationDate;
}
