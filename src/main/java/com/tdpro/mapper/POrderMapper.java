package com.tdpro.mapper;

import com.tdpro.entity.POrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface POrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(POrder record);

    int insertSelective(POrder record);

    POrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(POrder record);

    int updateByPrimaryKey(POrder record);

    POrder findByOrderNo(@Param("orderNo") String orderNo);

    BigDecimal sumRealPrice(@Param("uid") Long uid,@Param("id") Long id);

    int sumSuitOrderNum(@Param("uid") Long uid,@Param("id") Long id);

    List<POrder> findOrderRealPriceByStrawUid(@Param("strawUid") Long strawUid, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int updateOrderNotTake(@Param("takeTime") Date takeTime);

    int updateOrderNotPay(@Param("createTime") Date createTime);
}