package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddLinkDto;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.domain.vo.LinkVo2;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult linkPage(Link link,Integer pageNum,Integer pageSize){
        PageVo pageVo = linkService.getLinkPage(link,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        linkService.save(link);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    public ResponseResult findLink(@PathVariable("id") Long linkId){
        Link link = linkService.getById(linkId);
        LinkVo2 linkVo2 = BeanCopyUtils.copyBean(link, LinkVo2.class);
        return ResponseResult.okResult(linkVo2);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkVo2 linkVo2){
        Link link = BeanCopyUtils.copyBean(linkVo2, Link.class);
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") Long linkId){
        linkService.removeById(linkId);
        return ResponseResult.okResult();
    }
}
