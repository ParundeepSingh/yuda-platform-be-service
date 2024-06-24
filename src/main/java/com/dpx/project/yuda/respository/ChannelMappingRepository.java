package com.dpx.project.yuda.respository;

import com.dpx.project.yuda.model.dao.ChannelMappingDao;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository class which interacts with `channel_mapping` table
 */
public interface ChannelMappingRepository extends JpaRepository<ChannelMappingDao, Integer> {

    /**
     * Method saves the ChannelMappingDao Entity.
     * @param propertyEvalDetailsDao
     * @return
     */
    ChannelMappingDao save(ChannelMappingDao propertyEvalDetailsDao);

    /**
     * Fetches the ChannelMappingDao by Channel Handle
     * @param channelHandle
     * @return
     */
    ChannelMappingDao findByChannelHandle(String channelHandle);

}
