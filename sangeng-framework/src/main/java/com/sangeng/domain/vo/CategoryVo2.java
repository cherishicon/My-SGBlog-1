package com.sangeng.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryVo2 {
    //描述
    private String description;
    private Long id;
    //分类名
    private String name;
    //状态0:正常,1禁用
    private String status;
}
