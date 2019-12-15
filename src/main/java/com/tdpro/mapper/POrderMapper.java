package com.tdpro.mapper;

import com.tdpro.entity.POrder;
import com.tdpro.entity.extend.OrderETD;
import com.tdpro.entity.extend.OrderPageETD;
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

    int updateOrderNotTake(@Param("seendTime") Date seendTime);

    int updateOrderNotPay(@Param("id") Long id);

    List<POrder> orderNotPayList(@Param("createTime") Date createTime);

    List<OrderPageETD> findPageList(OrderPageETD orderPageETD);

    OrderPageETD findOrderInfoById(Long id);

    List<OrderETD> selectListByUid(OrderETD orderETD);
}