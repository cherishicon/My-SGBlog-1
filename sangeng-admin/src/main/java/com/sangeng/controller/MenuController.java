package com.sangeng.controller;


import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Menu;
import com.sangeng.domain.entity.RoleMenu;
import com.sangeng.domain.vo.MenuTreeVo;
import com.sangeng.domain.vo.MenuVo;
import com.sangeng.domain.vo.MenuVo2;
import com.sangeng.domain.vo.RoleMenuTreeSelectVo;
import com.sangeng.service.MenuService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult menuList(Menu menu){
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult menuDetail(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        MenuVo2 menuVo2 = BeanCopyUtils.copyBean(menu, MenuVo2.class);
        return ResponseResult.okResult(menuVo2);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        if((menu.getParentId() == 0L) && (menu.getMenuType().equals("F") || menu.getMenuType().equals("C"))){
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，按钮和菜单不能成为目录");
        }
        if((menu.getMenuType().equals("F")) && (menuService.getById(menu.getParentId()).getMenuType().equals("M")) ){
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，按钮不能成为目录的子项");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteMenu(@PathVariable Long menuId){
        if(menuService.hasChild(menuId)){
            return ResponseResult.errorResult(500,"无法删除菜单：" + menuService.getById(menuId).getMenuName() + "存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }

    @GetMapping("/treeselect")
    public ResponseResult getTreeSelect(){
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> menuTreeVos = menuService.selectTreeSelect(menus);
        return ResponseResult.okResult(menuTreeVos);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getMenuTree(@PathVariable("id") Long roleId){
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> menuTreeVos = menuService.selectTreeSelect(menus);



        List<Long> checkedKeys = menuService.checkedKeysList(roleId);
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(roleMenuTreeSelectVo);
    }
}
