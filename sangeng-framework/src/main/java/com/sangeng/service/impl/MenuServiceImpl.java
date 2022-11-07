package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.MenuTreeVo;
import com.sangeng.mapper.MenuMapper;
import com.sangeng.service.MenuService;
import com.sangeng.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 13:33:07
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        // 管理员的情况
        if(id.equals(1L)){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);

    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if(userId.equals(1L)){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        menuLambdaQueryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        menuLambdaQueryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menuList = list(menuLambdaQueryWrapper);
        return menuList;
    }

    @Override
    public boolean hasChild(Long menuId) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getId,menuId);
        return count(menuLambdaQueryWrapper) != 0;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
        return childrenList;
    }

    @Override
    public List<MenuTreeVo> selectTreeSelect(List<Menu> menus) {
        List<MenuTreeVo> menuTreeVos = menus.stream()
                                            .map(menu -> new MenuTreeVo(menu.getId(),menu.getMenuName(),menu.getParentId(),null))
                                            .collect(Collectors.toList());
        List<MenuTreeVo> childrenList = menuTreeVos.stream()
                                                   .filter(o -> o.getParentId().equals(0L))
                                                   .map(menuTreeVo -> menuTreeVo.setChildren(getChildrenList(menuTreeVos, menuTreeVo)))
                                                   .collect(Collectors.toList());
        return childrenList;
    }

    private List<MenuTreeVo> getChildrenList(List<MenuTreeVo> menuTreeVos,MenuTreeVo menuTreeVo) {
        List<MenuTreeVo> childrenList = menuTreeVos.stream()
                                                   .filter(menuTreeVo1 -> Objects.equals(menuTreeVo1.getParentId(),menuTreeVo.getId()))
                                                   .map(menuTreeVo1 -> menuTreeVo1.setChildren(getChildrenList(menuTreeVos,menuTreeVo1)))
                                                   .collect(Collectors.toList());
        return childrenList;
    }

    @Override
    public List<Long> checkedKeysList(Long roleId) {

        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> roleMenus = roleMenuService.list(roleMenuLambdaQueryWrapper);
        List<Long> theCheckedKeysList = roleMenus.stream()
                                                .map(RoleMenu::getMenuId)
                                                .collect(Collectors.toList());
        return theCheckedKeysList;
    }
}
