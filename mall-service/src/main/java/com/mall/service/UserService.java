package com.mall.service;

import com.mall.pojo.Users;
import com.mall.pojo.bo.UserBo;

public interface UserService {

    /**
     * check whether the username has been existed in the database
     * @param userName user name to be queried.
     * @return true if the user name is in the database, otherwise fasle.
     */
    public boolean queryUserNameExists(String userName);

    /**
     * create a users object and write the information into the database
     * @param userBo
     * @return User Object created.
     */
    public Users createUser(UserBo userBo);

    /**
     * Query the database for the user specified with its password.
     * @param username
     * @param password
     * @return a Users Object if existed, otherwise null.
     * @see Users
     */
    public Users queryUserForLogin(String username, String password);
}
