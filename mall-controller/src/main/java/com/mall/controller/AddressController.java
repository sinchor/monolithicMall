package com.mall.controller;

import com.mall.config.I18nMessage;
import com.mall.pojo.UserAddress;
import com.mall.pojo.bo.AddressBO;
import com.mall.service.AddressService;
import com.mall.utils.MallJSONResult;
import com.mall.utils.MobileEmailUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "地址相关的接口")
@RestController
@RequestMapping("/address")
public class AddressController {
    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1. 查询用户的所有收货地址列表
     * 2. 新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */
    final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "根据用户ID查询收货地址")
    @PostMapping("/list")
    public MallJSONResult list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg("");
        }

        List<UserAddress> list = addressService.queryAll(userId);
        return MallJSONResult.ok(list);
    }

    @PostMapping("/add")
    public MallJSONResult add(@RequestBody AddressBO addressBO) {

        MallJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);

        return MallJSONResult.ok();
    }
    private MallJSONResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return MallJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return MallJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return MallJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return MallJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return MallJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return MallJSONResult.errorMsg("收货地址信息不能为空");
        }

        return MallJSONResult.ok();
    }

    @PostMapping("/update")
    public MallJSONResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return MallJSONResult.errorMsg(I18nMessage.message("address.error.parameter_null"));
        }

        MallJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);

        return MallJSONResult.ok();
    }

    @PostMapping("/delete")
    public MallJSONResult delete(@RequestParam String userId, @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("address.error.parameter_null"));
        }

        addressService.deleteUserAddress(userId, addressId);

        return MallJSONResult.ok();
    }

    @PostMapping("/setDefault")
    public MallJSONResult setDefault(@RequestParam String userId, @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return MallJSONResult.errorMsg(I18nMessage.message("address.error.parameter_null"));
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);

        return MallJSONResult.ok();
    }
}
