package com.tdpro.mapper;

import com.tdpro.entity.PPayConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PPayConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PPayConfig record);

    int insertSelective(PPayConfig record);

    PPayConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PPayConfig record);

    int updateByPrimaryKey(PPayConfig record);

    PPayConfig findByChannelAndPayType(@Param("channel") Byte channel,@Param("payType") Byte payType);
}