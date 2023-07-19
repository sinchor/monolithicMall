package com.mall.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.mall.enums.YesOrNo;
import com.mall.mapper.ItemsCommentsMapperExt;
import com.mall.mapper.OrderItemsMapper;
import com.mall.mapper.OrderStatusMapper;
import com.mall.mapper.OrdersMapperExt;
import com.mall.pojo.OrderItems;
import com.mall.pojo.OrderItemsExample;
import com.mall.pojo.OrderStatus;
import com.mall.pojo.Orders;
import com.mall.pojo.bo.center.OrderItemsCommentBO;
import com.mall.pojo.vo.MyCommentVO;
import com.mall.service.center.MyCommentsService;
import com.mall.service.utils.ServiceHelper;
import com.mall.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MyCommentsServiceImpl implements MyCommentsService {

    final OrderItemsMapper orderItemsMapper;
    final ItemsCommentsMapperExt itemsCommentsMapper;
    final OrdersMapperExt ordersMapperExt;
    final OrderStatusMapper orderStatusMapper;

    final Sid sid;

    @Autowired
    public MyCommentsServiceImpl(OrderItemsMapper orderItemsMapper, ItemsCommentsMapperExt itemsCommentsMapperExt, OrdersMapperExt ordersMapperExt, OrderStatusMapper orderStatusMapper, Sid sid) {
        this.orderItemsMapper = orderItemsMapper;
        this.itemsCommentsMapper = itemsCommentsMapperExt;
        this.ordersMapperExt = ordersMapperExt;
        this.orderStatusMapper = orderStatusMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {

        OrderItemsExample example = new OrderItemsExample();
        OrderItemsExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        List<OrderItems> orderItems = orderItemsMapper.selectByExample(example);

        return orderItems;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        // 1. 保存评价 items_comments
        for (OrderItemsCommentBO orderItemsCommentBO : commentList) {
            orderItemsCommentBO.setItemId(sid.nextShort());
        }
        Map<String, Object> map = new TreeMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapper.saveComments(map);

        // 2. 修改订单表改已评价 orders
        Orders order = new Orders();
        order.setId(orderId);
        order.setIsComment(YesOrNo.YES.type);
        ordersMapperExt.updateByPrimaryKeySelective(order);

        // 3. 修改订单状态表的留言时间 order_status
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new TreeMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> myCommentVOS = itemsCommentsMapper.queryMyComments(map);
        PageHelper.clearPage();
        return ServiceHelper.setPagedGridResult(myCommentVOS);
    }
}
