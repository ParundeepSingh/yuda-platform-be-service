package com.dpx.project.yuda.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * DAO Class for Video Details Table.
 */
@Entity
@Data
@Table(name = "video_details")
@Accessors(chain = true)
public class VideoDetailsDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime = LocalDateTime.now();

    @Column(name = "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @Column(name = "publication_datetime")
    private LocalDateTime publicationDatetime;

    @Column(name = "is_deleted")
    private int isDeleted;

    @Column(name = "channel_id")
    private String channelId;
}
