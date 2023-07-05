package com.mall.service.impl;

import com.mall.enums.Sex;
import com.mall.mapper.UsersMapper;
import com.mall.pojo.Users;
import com.mall.pojo.UsersExample;
import com.mall.pojo.bo.UserBo;
import com.mall.service.UserService;
import com.mall.utils.CommonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    final UsersMapper usersMapper;
    private Sid sid;

    private static final String FACE_URL = "default_face.png";

    @Autowired
    public UserServiceImpl(UsersMapper usersMapper, Sid sid) {
        this.usersMapper = usersMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameExists(String userName) {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria usersExampleCriteria = usersExample.createCriteria();
        usersExampleCriteria.andUsernameEqualTo(userName);
        List<Users> users = usersMapper.selectByExample(usersExample);

        return users.isEmpty()? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBo userBo) {
        Users user = new Users();
        user.setUsername(userBo.getUsername());
        try {
            user.setPassword(CommonUtil.encodeByMd5(userBo.getPassword()));
            user.setBirthday(DateUtils.parseDate("1970-01-01","yyyy-MM-dd"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | ParseException e) {
            throw new RuntimeException(e);
        }
        user.setNickname(userBo.getUsername());
        user.setFace(FACE_URL);
        user.setSex(Sex.secret.type);

        Date date = new Date();
        user.setCreatedTime(date);
        user.setUpdatedTime(date);

        String userID = sid.nextShort();
        user.setId(userID);

        usersMapper.insert(user);
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        UsersExample usersExample = new UsersExample();
        UsersExample.Criteria criteria = usersExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        try {
            criteria.andPasswordEqualTo(CommonUtil.encodeByMd5(password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        List<Users> usersList = usersMapper.selectByExample(usersExample);
        if (usersList.isEmpty()) {
            return null;
        }
        return usersList.get(0);
    }
}
