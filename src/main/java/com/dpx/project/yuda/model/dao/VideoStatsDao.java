package com.dpx.project.yuda.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * DAO Class for Video Stats Table.
 */
@Entity
@Data
@Table(name = "video_stats")
@Accessors(chain = true)
public class VideoStatsDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "like_count")
    private Long likeCount;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

}
