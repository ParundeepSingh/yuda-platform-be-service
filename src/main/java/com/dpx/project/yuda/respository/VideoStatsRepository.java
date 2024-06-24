package com.dpx.project.yuda.respository;

import com.dpx.project.yuda.model.dao.VideoStatsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository class which interacts with `video_stats` table
 */
public interface VideoStatsRepository extends JpaRepository<VideoStatsDao, Integer> {

    /**
     * Method saves the VideoStatsDao Entity.
     * @param videoStatsDao
     * @return
     */
    VideoStatsDao save(VideoStatsDao videoStatsDao);


    @Query(value = "SELECT * FROM video_stats as vs WHERE vs.video_id = ?1 ORDER BY vs.created_datetime DESC LIMIT ?2",
            nativeQuery = true)
    public List<VideoStatsDao> getLatestRecordsByVideoId(String videoId, int limit);
}
