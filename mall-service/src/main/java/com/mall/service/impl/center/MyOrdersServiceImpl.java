package com.mall.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.mall.enums.OrderStatusEnum;
import com.mall.enums.YesOrNo;
import com.mall.mapper.OrderStatusMapper;
import com.mall.mapper.OrdersMapperExt;
import com.mall.pojo.OrderStatus;
import com.mall.pojo.OrderStatusExample;
import com.mall.pojo.Orders;
import com.mall.pojo.OrdersExample;
import com.mall.pojo.vo.MyOrdersVO;
import com.mall.pojo.vo.OrderStatusCountsVO;
import com.mall.service.center.MyOrdersService;
import com.mall.service.utils.ServiceHelper;
import com.mall.utils.PagedGridResult;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MyOrdersServiceImpl implements MyOrdersService {

    final OrdersMapperExt ordersMapper;
    final OrderStatusMapper orderStatusMapper;

    @Autowired
    public MyOrdersServiceImpl(OrdersMapperExt ordersMapper, OrderStatusMapper orderStatusMapper) {
        this.ordersMapper = ordersMapper;
        this.orderStatusMapper = orderStatusMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new TreeMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);
        List<MyOrdersVO> myOrdersVOS = ordersMapper.queryMyOrders(map);
        PageHelper.clearPage();

        return ServiceHelper.setPagedGridResult(myOrdersVOS);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus os = new OrderStatus();
        os.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        os.setDeliverTime(new Date());

        OrderStatusExample example = new OrderStatusExample();
        OrderStatusExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        criteria.andOrderStatusEqualTo(OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(os, example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andIsDeleteEqualTo(YesOrNo.NO.type);
        criteria.andIdEqualTo(orderId);
        List<Orders> orders = ordersMapper.selectByExample(ordersExample);
        if (orders != null) {
            return orders.get(0);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        OrderStatusExample example = new OrderStatusExample();
        OrderStatusExample.Criteria criteria = example.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        criteria.andOrderStatusEqualTo(OrderStatusEnum.WAIT_RECEIVE.type);
        int i = orderStatusMapper.updateByExampleSelective(orderStatus, example);
        return i == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        OrdersExample example = new OrdersExample();
        OrdersExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(orderId);
        criteria.andUserIdEqualTo(userId);

        int i = ordersMapper.updateByExampleSelective(orders, example);

        return i == 1 ? true : false;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {

        Map<String, Object> map = new TreeMap<>();
        map.put("userId", userId);
        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = ordersMapper.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNo.NO.type);
        int waitCommentCounts = ordersMapper.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitCommentCounts);
        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new TreeMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> myOrderTrend = ordersMapper.getMyOrderTrend(map);
        PageHelper.clearPage();

        return ServiceHelper.setPagedGridResult(myOrderTrend);
    }
}
