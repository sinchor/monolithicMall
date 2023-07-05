package com.mall.mapper;

import com.mall.pojo.Users;
import com.mall.pojo.UsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    long countByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int deleteByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int insert(Users row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int insertSelective(Users row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    List<Users> selectByExample(UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    Users selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int updateByExampleSelective(@Param("row") Users row, @Param("example") UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int updateByExample(@Param("row") Users row, @Param("example") UsersExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int updateByPrimaryKeySelective(Users row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table users
     *
     * @mbg.generated Sun Jul 02 12:01:08 CST 2023
     */
    int updateByPrimaryKey(Users row);
}