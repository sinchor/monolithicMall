package com.mall.controller.center;

import com.mall.pojo.Orders;
import com.mall.pojo.vo.OrderStatusCountsVO;
import com.mall.service.center.MyOrdersService;
import com.mall.utils.MallJSONResult;
import com.mall.utils.PagedGridResult;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "我的订单相关接口")
@RestController
@RequestMapping("/myorders")
public class MyOrdersController {

    final MyOrdersService myOrdersService;

    @Autowired
    public MyOrdersController(MyOrdersService myOrdersService) {
        this.myOrdersService = myOrdersService;
    }

    @PostMapping("/query")
    public MallJSONResult query(@RequestParam String userId, @RequestParam(required = false) Integer orderStatus,
                                Integer page, Integer pageSize){
        if (StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        PagedGridResult pagedGridResult = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);
        return MallJSONResult.ok(pagedGridResult);
    }

    @GetMapping("/deliver")
    public MallJSONResult deliver(@RequestParam String orderId) {

        if (org.apache.commons.lang3.StringUtils.isBlank(orderId)) {
            return MallJSONResult.errorMsg(null);
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return MallJSONResult.ok();
    }

    @PostMapping("/confirmReceive")
    public MallJSONResult confirmReceive(
            @Parameter(name = "orderId", required = true)
            @RequestParam String orderId,
            @Parameter(name = "userId", required = true)
            @RequestParam String userId) {

        MallJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return MallJSONResult.errorMsg("订单确认收货失败！");
        }

        return MallJSONResult.ok();
    }
    @PostMapping("/delete")
    public MallJSONResult delete(
            @Parameter(name = "orderId", required = true)
            @RequestParam String orderId,
            @Parameter(name = "userId", required = true)
            @RequestParam String userId) {

        MallJSONResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return MallJSONResult.errorMsg("订单删除失败！");
        }

        return MallJSONResult.ok();
    }

    @PostMapping("/statusCounts")
    public MallJSONResult statusCounts( @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg(null);
        }

        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);

        return MallJSONResult.ok(result);
    }

    @PostMapping("/trend")
    public MallJSONResult trend(@RequestParam String userId, Integer page, Integer pageSize) {
        if (org.apache.commons.lang3.StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg("");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        PagedGridResult ordersTrend = myOrdersService.getOrdersTrend(userId, page, pageSize);
        return MallJSONResult.ok(ordersTrend);
    }

    private MallJSONResult checkUserOrder(String userID, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userID, orderId);
        if (orders == null) {
            return MallJSONResult.errorMsg("");
        }
        return MallJSONResult.ok(orders);
    }
}
