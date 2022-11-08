package com.sangeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sangeng.constants.SystemConstants;
import com.sangeng.domain.ResponseResult;
import com.sangeng.domain.entity.Link;
import com.sangeng.domain.vo.LinkVo;
import com.sangeng.domain.vo.LinkVo2;
import com.sangeng.domain.vo.PageVo;
import com.sangeng.mapper.LinkMapper;
import com.sangeng.service.LinkService;
import com.sangeng.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-10-30 12:42:44
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);

        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public PageVo getLinkPage(Link link, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Link> linkLambdaQueryWrapper = new LambdaQueryWrapper<>();
        linkLambdaQueryWrapper.like(StringUtils.hasText(link.getName()),Link::getName,link.getName());
        linkLambdaQueryWrapper.eq(StringUtils.hasText(link.getStatus()),Link::getStatus,link.getStatus());

        Page<Link> linkPage = new Page<>();
        linkPage.setCurrent(pageNum);
        linkPage.setSize(pageSize);
        page(linkPage,linkLambdaQueryWrapper);

        List<Link> links = linkPage.getRecords();
        List<LinkVo2> linkVo2List = links.stream()
                                        .map(link1 -> BeanCopyUtils.copyBean(link1, LinkVo2.class))
                                        .collect(Collectors.toList());
        return new PageVo(linkVo2List,linkPage.getTotal());
    }
}
