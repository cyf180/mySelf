package com.tdpro.mapper;

import com.tdpro.entity.PUser;
import com.tdpro.entity.extend.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface PUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PUser record);

    int insertSelective(PUser record);

    PUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PUser record);

    int updateByPrimaryKey(PUser record);

    UserETD getUserCentre(Long id);

    int getTeamPeopleNum(Long id);

    List<UserTeamETD> userTeamList(Long id);

    int updateBalance(UserBalanceUpdateETD userBalanceUpdateETD);

    PUser findByPhone(@Param("phone") String phone);

    List<PUser> findListIsUser();

    List<UserPageETD> selectPageList(UserPageETD userPage);

    UserInfoETD findInfoById(Long id);

    PUser findOne();
}