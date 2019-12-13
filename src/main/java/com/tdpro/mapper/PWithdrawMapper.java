package com.tdpro.mapper;

import com.tdpro.entity.PWithdraw;
import com.tdpro.entity.extend.WithdrawDO;
import com.tdpro.entity.extend.WithdrawETD;
import com.tdpro.entity.extend.WithdrawPageETD;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PWithdrawMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PWithdraw record);

    int insertSelective(PWithdraw record);

    PWithdraw selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PWithdraw record);

    int updateByPrimaryKey(PWithdraw record);

    List<WithdrawETD> getUserList(WithdrawETD withdrawETD);

    WithdrawDO sumWithdraw(WithdrawPageETD record);

    List<WithdrawPageETD> selectPageList(WithdrawPageETD record);
}