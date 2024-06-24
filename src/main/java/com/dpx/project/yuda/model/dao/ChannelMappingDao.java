package com.dpx.project.yuda.model.dao;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * DAO Class for Channel Mapping Table.
 */
@Entity
@Data
@Table(name = "channel_mapping")
@Accessors(chain = true)
public class ChannelMappingDao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "channel_handle")
    private String channelHandle;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "created_datetime")
    private LocalDateTime createdDatetime;

    @Column(name = "modified_datetime")
    private LocalDateTime modifiedDateTime;

    @Column(name = "is_active")
    private int isActive;
}
