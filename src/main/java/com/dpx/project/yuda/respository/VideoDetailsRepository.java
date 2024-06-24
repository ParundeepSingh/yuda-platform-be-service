package com.dpx.project.yuda.respository;

import com.dpx.project.yuda.model.dao.VideoDetailsDao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class which interacts with `video_details` table
 */
public interface VideoDetailsRepository extends JpaRepository<VideoDetailsDao, Integer> {

    /**
     * Fetches the VideoDetailsDao by Video ID.
     * @param videoId
     * @return
     */
    VideoDetailsDao findByVideoId(String videoId);

    /**
     * Method saves the VideoDetailsDao Entity.
     * @param entity
     * @return
     */
    VideoDetailsDao save(VideoDetailsDao entity);
}
