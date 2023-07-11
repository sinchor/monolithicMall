package com.mall.mapper;

import com.mall.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperExt extends ItemsMapper{

    public List<ItemCommentVO> queryItemComments(@Param("paramMap") Map<String, Object> map);
}
