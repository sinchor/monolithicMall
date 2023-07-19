package com.mall.controller.center;

import com.mall.enums.YesOrNo;
import com.mall.pojo.OrderItems;
import com.mall.pojo.Orders;
import com.mall.pojo.bo.center.OrderItemsCommentBO;
import com.mall.service.center.MyCommentsService;
import com.mall.service.center.MyOrdersService;
import com.mall.utils.MallJSONResult;
import com.mall.utils.PagedGridResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mycomments")
public class MyCommentsController {
    final MyCommentsService myCommentsService;
    final MyOrdersService myOrdersService;

    @Autowired
    public MyCommentsController(MyCommentsService myCommentsService, MyOrdersService myOrdersService) {
        this.myCommentsService = myCommentsService;
        this.myOrdersService = myOrdersService;
    }

    @Operation(summary = "查询订单列表")
    @PostMapping("/pending")
    public MallJSONResult pending(
            @Parameter(name = "userId", required = true)
            @RequestParam String userId,
            @Parameter(name = "orderId", required = true)
            @RequestParam String orderId) {

        // 判断用户和订单是否关联
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return MallJSONResult.errorMsg(null);
        }
        // 判断该笔订单是否已经评价过，评价过了就不再继续
        if (orders.getIsComment() == YesOrNo.YES.type) {
            return MallJSONResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> orderItems = myCommentsService.queryPendingComment(orderId);

        return MallJSONResult.ok(orderItems);
    }

    @PostMapping("/saveList")
    public MallJSONResult saveList(
            @RequestParam String userId,
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // 判断用户和订单是否关联
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            return MallJSONResult.errorMsg(null);
        }
        // 判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return MallJSONResult.errorMsg("评论内容不能为空！");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return MallJSONResult.ok();
    }

    @PostMapping("/query")
    public MallJSONResult query(
            @RequestParam String userId,
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return MallJSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        PagedGridResult grid = myCommentsService.queryMyComments(userId,
                page,
                pageSize);

        return MallJSONResult.ok(grid);
    }

}
