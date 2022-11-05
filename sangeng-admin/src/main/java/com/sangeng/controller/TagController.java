package com.sangeng.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.api.R;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddTagDto;
import com.sangeng.domain.dto.EditTagDto;
import com.sangeng.domain.dto.TagListDto;
import com.sangeng.domain.entity.Tag;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.domain.vo.TagVo;
import com.sangeng.domain.vo.TagVo2;
import com.sangeng.service.TagService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> tags = tagService.listAllTag();
        return ResponseResult.okResult(tags);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTagDto){
        Tag tag = BeanCopyUtils.copyBean(addTagDto, Tag.class);
        tagService.save(tag);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult delTag(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable(value = "id") Long id){
        Tag tag = tagService.getById(id);
//        TagVo2 tagVo2 = BeanCopyUtils.copyBean(tag, TagVo2.class);
        EditTagDto editTagDto = BeanCopyUtils.copyBean(tag, EditTagDto.class);
        return ResponseResult.okResult(editTagDto);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody EditTagDto editTagDto){
        Tag tag = BeanCopyUtils.copyBean(editTagDto, Tag.class);
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }

}
