package com.mall.mapper;


import com.mall.pojo.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperExt extends ItemsCommentsMapper{

    public void saveComments(@Param("paramsMap") Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);

}