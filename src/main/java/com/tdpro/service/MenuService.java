package com.tdpro.service;


import com.tdpro.common.utils.MenuItem;
import com.tdpro.entity.extend.MenuETD;
import com.tdpro.service.Base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @About
 * @Author jy
 * @Date 2018/5/29 17:55
 */
public interface MenuService extends BaseService<MenuETD> {
    List<MenuItem> loadMenus(Long rid, String pid, String url);

    List selectList(Map<String, String> param);

    boolean addOrUpdate(String updateP, MenuETD menu, MenuETD itemMenu);
    int getCount(MenuETD menu);
    int deletes(String ids, String deleteParent);
    Map loadUserMenu(Long rid);

    /**
     * 验证url是否允许该角色用户访问
     * @param url
     * @param rid
     * @return
     */
    Boolean verifyUrl(String url, Long rid);
}
