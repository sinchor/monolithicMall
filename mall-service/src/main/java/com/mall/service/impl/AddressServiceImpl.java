package com.mall.service.impl;

import com.mall.enums.YesOrNo;
import com.mall.mapper.UserAddressMapper;
import com.mall.pojo.UserAddress;
import com.mall.pojo.UserAddressExample;
import com.mall.pojo.bo.AddressBO;
import com.mall.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    final UserAddressMapper userAddressMapper;
    final Sid sid;
    @Autowired
    public AddressServiceImpl(UserAddressMapper userAddressMapper, Sid sid) {
        this.userAddressMapper = userAddressMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddressExample example = new UserAddressExample();
        UserAddressExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(example);
        return userAddresses;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为‘默认地址’
        Integer isDefault = 0;
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        if (addressList == null || addressList.isEmpty() || addressList.size() == 0) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        // 2. 保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);

        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        Date date = new Date();
        newAddress.setCreatedTime(date);
        newAddress.setUpdatedTime(date);

        userAddressMapper.insert(newAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressBO.getAddressId());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    public void deleteUserAddress(String userId, String addressId) {

        userAddressMapper.deleteByPrimaryKey(addressId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        //query the default address for the userId
        UserAddressExample queryDefault = new UserAddressExample();
        UserAddressExample.Criteria criteria = queryDefault.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andIsDefaultEqualTo(YesOrNo.YES.type);
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(queryDefault);

        //remove the default property for the address queried.
        for (UserAddress ua : userAddresses) {
            ua.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKey(ua);
        }

        //set default address
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKey(userAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddres(String userId, String addressId) {
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);
        return userAddress;
    }
}
