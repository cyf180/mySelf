package com.tdpro.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.tdpro.common.utils.MenuItem;
import com.tdpro.common.utils.MenuType;
import com.tdpro.entity.PMenu;
import com.tdpro.entity.extend.MenuETD;
import com.tdpro.mapper.PMenuMapper;
import com.tdpro.mapper.PRoleMenuMapper;
import com.tdpro.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
    @Autowired
    private PMenuMapper menuMapper;
    @Autowired
    private PRoleMenuMapper roleMenuMapper;
    @Override
    public List<MenuItem> loadMenus(Long rid, String pid, String url) {
        Map<String,String> param = new HashMap<String, String>();
        if (rid != null) {
            param.put("rid", rid.toString());// 角色ID
        }
        param.put("mid", pid);// 菜单父ID
        List<MenuETD> menus = menuMapper.selectListByRid(param);
        // 创建菜单集合
        List<MenuItem> root = new ArrayList<MenuItem>();
        // 循环添加菜单到菜单集合
        for(int i=0;i<menus.size();i++){
            MenuETD menu = menus.get(i);
            MenuItem item = new MenuItem(menu.getMenuName(), null);
            item.setId(menu.getId());
            item.setMid(menu.getMid());
            item.setMenuType(menu);
            if (url != null) {
                item.setUrl(url);
            } else {
                item.setUrl(menu.getMenuUrl());
            }
            root.add(item);
        }
        // 加载子菜单，注意 只加载type为模块级或页面级的
        for (int i = 0; i < root.size(); i++) {
            MenuItem ee = root.get(i);
            if(ee.getType()==null || ee.getType().toString().equals("") || ee.getType().equals(MenuType.button)){
                continue;
            }

            MenuETD mm = new MenuETD();
            mm.setMid(ee.getId());
            loadChildrenByPid(ee, mm, url, rid);
        }

        return root;
    }

    @Override
    public List selectList(Map<String, String> param) {
        return menuMapper.selectList(param);
    }

    @Override
    public boolean addOrUpdate(String updateP, MenuETD menu, MenuETD itemMenu) {
        if (itemMenu != null) {
            // 添加子菜单
            insertSelective(itemMenu);
        }
        // 修改父菜单
        updateByPrimaryKeySelective(menu);
        return true;
    }

    @Override
    public int getCount(MenuETD menu) {
        return menuMapper.getCount(menu);
    }

    @Override
    public int deletes(String ids, String deleteParent) {
        String[] idArr = ids.split(",");
        // 按照从小到大排序
        Arrays.sort(idArr, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int a1 = Integer.parseInt(o1);
                int a2 = Integer.parseInt(o2);
                if (a1 > a2) {
                    return 1;
                } else if (a1 < a2) {
                    return -1;
                }
                return 0;
            }
        });
        MenuETD menu = new MenuETD();

        if (deleteParent.equals("-1")) {
            // 从菜单ID最小的开始删起，避免先把ID大的删除了，倒置ID小的成为了叶子节点而被删除掉
            for (int i = 0; i < idArr.length; i++) {
				/*
				 * 1、菜单下没有子菜单，直接删除 2、菜单下有子菜单，检查所有的子菜单是否全部已经勾选 A)全部勾选，则可以删除。
				 * B)没有全部勾选，则不能删除。
				 */
                menu.clear();
                menu.setMid(Long.valueOf(idArr[i]));
                if (this.getCount(menu) == 0) {
                    // 指定节点下没有子菜单，删除指定的菜单(叶子)
                    menu.clear();
                    menu.setId(Long.valueOf(idArr[i]));
                    this.deleteByPrimaryKey(menu.getId());
                }
            }
        } else if (deleteParent.equals("1")) {
            for (int i = idArr.length - 1; i >= 0; i--) {
				/*
				 * 1、菜单下没有子菜单，直接删除 2、菜单下有子菜单，检查所有的子菜单是否全部已经勾选 A)全部勾选，则可以删除。
				 * B)没有全部勾选，则不能删除。
				 */
                menu.clear();
                menu.setMid(Long.valueOf(idArr[i]));
                if (this.getCount(menu) == 0) {
                    // 指定节点下没有子菜单，删除指定的菜单(叶子)
                    menu.clear();
                    menu.setId(Long.valueOf(idArr[i]));
                    this.deleteByPrimaryKey(menu.getId());
                } else {
                    menu.clear();
                    menu.setMid(Long.valueOf(idArr[i]));
                    // 查询指定菜单下的全部子菜单
                    List<MenuETD> menus = this.selectList(menu);
                    if (menus != null && menus.size() > 0) {
                        if (checkAllContains(idArr, menus)) {
                            this.deleteByPrimaryKey(menu.getId());
                        }
                    }
                }
            }
        } else {
            throw new NullPointerException("deleteParent:" + deleteParent);
        }
        return 1;
    }
    /**
     * 检查指定的菜单列表是否全部存在于另一个列表中
     *
     * @param idArr
     *            待删除的菜单列表
     * @param list
     *            被检查的菜单列表
     * @return 全部存在返回true，否则返回false
     */
    private boolean checkAllContains(String[] idArr, List<MenuETD> list) {
        int n = list.size();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < idArr.length; j++) {
                if (list.get(i).getId().equals(idArr[j])) {
                    n--;
                    break;
                }
            }
        }
        return n == 0 ? true : false;
    }
    /**
     * 根据父ID加载子节点
     * @param item
     * @param menu
     * @param url
     * @param rid
     */
    private void loadChildrenByPid(MenuItem item, MenuETD menu, String url, Long rid) {
        Map<String, String> param = new HashMap<String, String>();
        if (rid != null) {
            param.put("rid", rid.toString());
        }
        param.put("mid", menu.getMid().toString());
        // 加载菜单节点
        List<MenuETD> data = menuMapper.selectListByRid(param);
        if (data == null || data.size() == 0) {
            return;
        }
        if (item.getChildren() == null) {
            item.setChildren(new ArrayList<MenuItem>());
        }
        // 创建菜单节点
        for (int i = 0; i < data.size(); i++) {
            MenuETD entry = data.get(i);
            MenuItem addItem = new MenuItem(entry.getMenuName(), null);
            addItem.setId(entry.getId());
            addItem.setMid(entry.getMid());
            addItem.setMenuType(entry);
            if (url != null) {
                addItem.setUrl(url);
            } else {
                addItem.setUrl(entry.getMenuUrl());
            }
            item.getChildren().add(addItem);
        }
        // 根据菜单节点进行递归加载
        for (int i = 0; i < item.getChildren().size(); i++) {
            MenuETD itemMenu = new MenuETD();
            itemMenu.setMid(item.getChildren().get(i).getId());
            loadChildrenByPid(item.getChildren().get(i), itemMenu, url, rid);
        }
    }

    @Override
    public int insert(MenuETD record) {
        return 0;
    }

    @Override
    public int insertSelective(MenuETD record) {
        return menuMapper.insertSelective(record);
    }

    @Override
    public MenuETD selectByPrimaryKey(Long id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<MenuETD> selectList(MenuETD record) {
        return null;
    }

    @Override
    public PageInfo selectPageList(MenuETD record) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(MenuETD record) {
        return menuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(MenuETD record) {
        return menuMapper.updateByPrimaryKey(record);
    }

    @Override
    public int deleteByPrimaryKey(Long id, Long uId, String ip) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        roleMenuMapper.deleteWithMenu(id);
        return menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deletes(Integer[] ids) {
        return 0;
    }

    /**
     * loadUserMenu
     */
    @Override
    public Map loadUserMenu(Long rid) {
        Map<String, Object> allmenu = new HashMap<String, Object>();
        //加载用户菜单
        Collection<MenuItem> userMenus = loadMenus(rid);
        allmenu.put("userMenus", userMenus);
        /*
		 * 首先，加载顶级目录或页面菜单
		 */
        Map param = new HashMap<String, String>();
        if (rid != null) {
            param.put("rid", rid);//角色ID
        }
        List<MenuETD> menus = menuMapper.selectList(param);
        //创建按钮菜单集合
        LinkedHashMap<String, MenuItem> buttonMe = new LinkedHashMap<String, MenuItem>();
        //创建普通菜单集合
        LinkedHashMap<String, MenuItem> nomarlMe = new LinkedHashMap<String, MenuItem>();
        //循环添加菜单到菜单集合
        for (MenuETD menu : menus) {
            MenuItem item = new MenuItem(menu.getMenuName(), null);
            item.setId(menu.getId());
            item.setMid(menu.getMid());
            item.setMenuType(menu);
            item.setUrl(StringUtils.trimToEmpty(menu.getMenuUrl()));
            item.setApiUrl(StringUtils.trimToEmpty(menu.getMenuApiPath()));
            if(item.isButton()) {
                buttonMe.put(item.getId().toString(), item);
            }
            if(!item.isButton()&&!item.isRooMenu()){
                nomarlMe.put(item.getId().toString(), item);
            }
        }
        //判断按钮权限
        Collection<MenuItem> buttonMenus=buttonMe.values();
        //TreeMap<String, String>();
        JSONObject button =  new JSONObject();
        for(MenuItem userMenu:buttonMenus){
            button.put(userMenu.getUrl(), userMenu.getUrl());
        }
        allmenu.put("userBottons", button);
        return allmenu;
    }

    @Override
    public Boolean verifyUrl(String url, Long rid) {
        if (StringUtils.isEmpty(url)||rid==null) {
            return false;
        }
        String str="\\d+$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()){
            url=url.replace(matcher.group(),"");
        }
        Map param = new HashMap<String, String>();
        //角色ID
        param.put("rid", rid);
        param.put("url", url);

        PMenu menus = menuMapper.selectByRid(param);
        if (menus!=null&& StringUtils.isNotEmpty(menus.getMenuApiPath())){
            return true;
        }
        return false;
    }

    private Collection<MenuItem> loadMenus(Long rid) {
		/*
		 * 首先，加载顶级目录或页面菜单
		 */
        Map param = new HashMap<String, String>();
        if (rid != null) {
            param.put("rid", rid);//角色ID
        }
//		param.put("pid", pid);//菜单父ID
        List<MenuETD> menus = menuMapper.selectList(param);
        //创建菜单集合
        LinkedHashMap<Long, MenuItem> root = new LinkedHashMap<Long, MenuItem>();
        //循环添加菜单到菜单集合
        for (MenuETD menu : menus) {
            MenuItem item = new MenuItem(menu.getMenuName(), null);
            item.setId(menu.getId());
            item.setMid(menu.getMid());
            item.setMenuType(menu);
            item.setUrl(StringUtils.trimToEmpty(menu.getMenuUrl()));
            if(item.isRooMenu()) {
                root.put(item.getId(), item);
            }
        }
        for (MenuETD menu : menus) {
            MenuItem item = new MenuItem(menu.getMenuName(), null);
            item.setId(menu.getId());
            item.setMid(menu.getMid());
            item.setMenuType(menu);
            item.setUrl(StringUtils.trimToEmpty(menu.getMenuUrl()));
            if(!item.isRooMenu() && !item.isButton()) {
                MenuItem parentItem = root.get(item.getMid());
                if(parentItem != null) {
                    parentItem.addClild(item);
                } else {
                    log.warn("菜单项{}({})没有对应的父级菜单", item.getName(), item.getId());
                }
            }
        }
        return root.values();
    }
}
