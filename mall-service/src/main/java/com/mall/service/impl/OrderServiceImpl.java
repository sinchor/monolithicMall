package com.mall.service.impl;

import com.mall.enums.OrderStatusEnum;
import com.mall.enums.YesOrNo;
import com.mall.mapper.OrderItemsMapper;
import com.mall.mapper.OrderStatusMapper;
import com.mall.mapper.OrdersMapper;
import com.mall.mapper.UserAddressMapper;
import com.mall.pojo.*;
import com.mall.pojo.bo.SubmitOrderBO;
import com.mall.pojo.vo.MerchantOrdersVO;
import com.mall.pojo.vo.OrderVO;
import com.mall.service.ItemService;
import com.mall.service.OrderService;
import com.mall.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    final OrdersMapper ordersMapper;
    final OrderItemsMapper orderItemsMapper;
    final UserAddressMapper userAddressMapper;
    final OrderStatusMapper orderStatusMapper;
    final ItemService itemService;
    final Sid sid;

    @Autowired
    public OrderServiceImpl(OrdersMapper ordersMapper, OrderItemsMapper orderItemsMapper,
                            UserAddressMapper userAddressMapper, OrderStatusMapper orderStatusMapper,
                            ItemService itemService, Sid sid) {
        this.ordersMapper = ordersMapper;
        this.orderItemsMapper = orderItemsMapper;
        this.userAddressMapper = userAddressMapper;
        this.orderStatusMapper = orderStatusMapper;
        this.itemService = itemService;
        this.sid = sid;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
        // 1. 新订单数据保存
        Orders newOrder = createNewOrder(submitOrderBO);

        // 2. 循环根据itemSpecIds保存订单商品信息表
        updateOrdersInfoAndDatabase(submitOrderBO, newOrder);

        // 3. 保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(newOrder.getId());
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(newOrder.getId());
        merchantOrdersVO.setMerchantUserId(submitOrderBO.getUserId());
        merchantOrdersVO.setAmount(newOrder.getRealPayAmount() + newOrder.getPostAmount());
        merchantOrdersVO.setPayMethod(submitOrderBO.getPayMethod());

        // 5. 构建自定义订单vo
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(newOrder.getId());
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    private void updateOrdersInfoAndDatabase(SubmitOrderBO submitOrderBO, Orders newOrder) {
        String itemSpecIdArr[] = submitOrderBO.getItemSpecIds().split(",");
        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计
        for (String itemSpecId : itemSpecIdArr) {

            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;

            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(newOrder.getId());
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemSpec.getName());
            subOrderItem.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);
    }

    private Orders createNewOrder(SubmitOrderBO submitOrderBO) {
        // 包邮费用设置为0
        Integer postAmount = 0;

        String orderId = sid.nextShort();

        UserAddress address = userAddressMapper.selectByPrimaryKey(submitOrderBO.getAddressId());

        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(submitOrderBO.getUserId());

        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " "
                + address.getCity() + " "
                + address.getDistrict() + " "
                + address.getDetail());

        newOrder.setPostAmount(postAmount);

        newOrder.setPayMethod(submitOrderBO.getPayMethod());
        newOrder.setLeftMsg(submitOrderBO.getLeftMsg());

        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        Date date = new Date();
        newOrder.setCreatedTime(date);
        newOrder.setUpdatedTime(date);
        return  newOrder;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setOrderStatus(orderStatus);
        os.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(os);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        return orderStatus;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        // 查询所有未付款订单，判断时间是否超时（1天），超时则关闭交易
        OrderStatusExample example = new OrderStatusExample();
        OrderStatusExample.Criteria criteria = example.createCriteria();
        criteria.andOrderStatusEqualTo(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatuses = orderStatusMapper.selectByExample(example);
        setOrderStatusClosed(orderStatuses);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void setOrderStatusClosed(List<OrderStatus> orderStatuses) {
        for (OrderStatus os : orderStatuses) {
            Date createdTime = os.getCreatedTime();
            if (!expired(createdTime, 1)) {
                continue;
            }
            os.setOrderStatus(OrderStatusEnum.CLOSE.type);
            os.setCloseTime(new Date());
            orderStatusMapper.updateByPrimaryKey(os);
        }
    }
    private boolean expired(Date createdTime, Integer days) {
        long diff = new Date().getTime() - createdTime.getTime();
        long convert = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if (convert < days) {
            return false;
        }
        return true;
    }
}
