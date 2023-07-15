package com.mall.controller;

import com.mall.config.I18nMessage;
import com.mall.enums.OrderStatusEnum;
import com.mall.enums.PayMethod;
import com.mall.pojo.OrderStatus;
import com.mall.pojo.bo.SubmitOrderBO;
import com.mall.pojo.vo.MerchantOrdersVO;
import com.mall.pojo.vo.OrderVO;
import com.mall.service.OrderService;
import com.mall.utils.MallJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Tag(name = "订单相关接口")
@RestController
@RequestMapping("/orders")
public class OrdersController {

    private static final String SHOPCART = "shopcart";
    final OrderService orderService;
    final HttpServletRequest httpServletRequest;
    final HttpServletResponse httpServletResponse;

    private String payReturnUrl;
    private String paymentUrl;

    final RestTemplate restTemplate;

    @Autowired
    public OrdersController(OrderService orderService,
                            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                            @Value("${app_config.url_config.default_return_url}") String payReturnUrl,
                            @Value("${app_config.url_config.default_payment_url}") String paymentUrl,
                            RestTemplate restTemplate) {
        this.orderService = orderService;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.payReturnUrl = payReturnUrl;
        this.paymentUrl = paymentUrl;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/create")
    public MallJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {
        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return MallJSONResult.errorMsg(I18nMessage.message("orders.error.paymethod_not_support"));
        }

        // 1. 创建订单
        OrderVO order = orderService.createOrder(submitOrderBO);
        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie

        //CookieUtils.setCookie(httpServletRequest, httpServletResponse, SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user","pay");
        headers.add("password","paypsw");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<MallJSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl,
                        entity,
                        MallJSONResult.class);
        MallJSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return MallJSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return MallJSONResult.ok(order.getOrderId());
    }

    @PostMapping("/getPaidOrderInfo")
    public MallJSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return MallJSONResult.ok(orderStatus);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }
}
