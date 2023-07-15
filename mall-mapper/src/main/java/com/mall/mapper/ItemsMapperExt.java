package com.mall.mapper;

import com.mall.pojo.vo.ItemCommentVO;
import com.mall.pojo.vo.SearchItemsVO;
import com.mall.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperExt extends ItemsMapper{

    public List<ItemCommentVO> queryItemComments(@Param("paramMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    public List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);
}
