package com.tdpro.service.Base;

import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.QueryModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Author: lijie
 * Email: admin@elevenstyle.com
 * Date: 2018/4/8
 * Time: 15:47
 */
public interface BaseService<E extends QueryModel> {

    public int insert(E record);

    public int insertSelective(E record);

    E selectByPrimaryKey(Long id);

    List<E> selectList(E record);

    PageInfo selectPageList(E record);

    int updateByPrimaryKeySelective(E record);

    int updateByPrimaryKey(E record);

    int deleteByPrimaryKey(Long id, Long uId, String ip);

    int deleteByPrimaryKey(Long id);


    int deletes(Integer[] ids);
}
