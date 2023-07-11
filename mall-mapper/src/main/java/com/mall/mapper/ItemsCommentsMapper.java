package com.mall.mapper;

import com.mall.pojo.ItemsComments;
import com.mall.pojo.ItemsCommentsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemsCommentsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    long countByExample(ItemsCommentsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int deleteByExample(ItemsCommentsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int insert(ItemsComments row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int insertSelective(ItemsComments row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    List<ItemsComments> selectByExample(ItemsCommentsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    ItemsComments selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int updateByExampleSelective(@Param("row") ItemsComments row, @Param("example") ItemsCommentsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int updateByExample(@Param("row") ItemsComments row, @Param("example") ItemsCommentsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int updateByPrimaryKeySelective(ItemsComments row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table items_comments
     *
     * @mbg.generated Thu Jul 06 23:41:41 CST 2023
     */
    int updateByPrimaryKey(ItemsComments row);
}