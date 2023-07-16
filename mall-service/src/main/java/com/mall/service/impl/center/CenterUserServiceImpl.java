package com.mall.service.impl.center;

import com.mall.mapper.UsersMapper;
import com.mall.pojo.Users;
import com.mall.pojo.bo.center.CenterUserBO;
import com.mall.service.center.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    final UsersMapper usersMapper;

    @Autowired
    public CenterUserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users user = new Users();
        BeanUtils.copyProperties(centerUserBO, user);
        user.setId(userId);
        user.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(user);

        return queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {

        Users users = new Users();
        users.setId(userId);
        users.setFace(faceUrl);
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }
}
