package com.mall.service.utils;

import com.github.pagehelper.PageInfo;
import com.mall.utils.PagedGridResult;

import java.util.List;

public class ServiceHelper {
    static public PagedGridResult setPagedGridResult(List<?> list) {

        PageInfo<?> pageInfo = new PageInfo<>(list);

        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(pageInfo.getPageNum());
        pagedGridResult.setTotal(pageInfo.getPages());
        pagedGridResult.setRecords(pageInfo.getTotal());
        pagedGridResult.setRows(pageInfo.getList());
        return pagedGridResult;
    }
}
