package com.sangeng.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddCategoryDto;
import com.sangeng.domain.entity.Category;
import com.sangeng.domain.vo.*;
import com.sangeng.enums.AppHttpCodeEnum;
import com.sangeng.service.CategoryService;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import com.sangeng.utils.WebUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author 三更  B站： https://space.bilibili.com/663528522
 */
@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

    @PreAuthorize("@ps.hasPermission('content:category:export')")  // 自定义权限，校验关键字
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> category = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(category, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult listPage(Category category,Integer pageNum,Integer pageSize){
        PageVo pageVo = categoryService.selectCategoryPage(category,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult newCategory(@RequestBody AddCategoryDto addCategoryDto){
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        categoryService.save(category);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult findCategory(@PathVariable("id") Long categoryId){
        Category category = categoryService.getById(categoryId);
        CategoryVo2 categoryVo2 = BeanCopyUtils.copyBean(category, CategoryVo2.class);
        return ResponseResult.okResult(categoryVo2);
    }

    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryVo2 categoryVo2){
        Category category = BeanCopyUtils.copyBean(categoryVo2, Category.class);
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable("id") Long categoryId){
        categoryService.removeById(categoryId);
        return  ResponseResult.okResult();
    }
}