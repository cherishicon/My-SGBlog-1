package com.sangeng.controller;

import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.dto.AddArticleDto;
import com.sangeng.domain.dto.ArticleDto;
import com.sangeng.domain.entity.Article;
import com.sangeng.domain.vo.ArticleVo;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.service.ArticleService;
import com.sangeng.utils.BeanCopyUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }

    @GetMapping("/list")
    public ResponseResult articleList(Article article,Integer pageNum,Integer pageSize){
        PageVo pageVo = articleService.selectArticlePage(article,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    @GetMapping("/{id}")
    public ResponseResult articleDetail(@PathVariable(value = "id") Long id){
        ArticleVo articleVo = articleService.getArticleInfo(id);
        return ResponseResult.okResult(articleVo);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody ArticleDto articleDto){
//        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
//        articleService.updateById(article);
        articleService.edit(articleDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
