package com.sangeng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sangeng.domain.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

public interface ArticleMapper extends BaseMapper<Article> {
    void updateViewCount(@Param("viewCount") Long viewCount,@Param("id") Long id);
}
