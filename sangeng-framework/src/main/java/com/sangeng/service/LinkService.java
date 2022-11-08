package com.sangeng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.PageVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-30 12:42:44
 */
public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    PageVo getLinkPage(Link link, Integer pageNum, Integer pageSize);
}
