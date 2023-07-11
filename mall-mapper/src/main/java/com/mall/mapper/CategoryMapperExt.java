package com.mall.mapper;

import com.mall.pojo.vo.CategoryVO;
import com.mall.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperExt extends CategoryMapper{
    public List<CategoryVO> getSubCategories(Integer fatherId);
    public List<NewItemsVO> getNewItems(@Param("paramMap") Map<String, Object> map);
}
