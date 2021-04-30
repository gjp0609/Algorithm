package com.onysakura.algorithm.api.epay;

import com.alibaba.fastjson.JSON;
import com.onysakura.algorithm.utilities.basic.DateUtils;
import com.onysakura.algorithm.utilities.basic.ParamsUtils;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientConstants;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientUtils;
import com.onysakura.algorithm.utilities.web.httpclient.PostParam;
import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;
import com.onysakura.algorithm.utilities.web.security.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class PayTest {
    public static void main(String[] args) throws Exception {
        String orderJsonStr = "";
        String privateKey = "";
        log.info("支付异步通知接收数据,订单内容：[{}]", orderJsonStr);
        Order order = JSON.parseObject(orderJsonStr, Order.class);
        OrderPayNotifyVO orderPayNotifyVO = new OrderPayNotifyVO();
        orderPayNotifyVO.setOrderNo(order.getOrderNo());// 支付平台订单号
        orderPayNotifyVO.setOutOrderNo(order.getOutOrderNo());// 商户订单号
        orderPayNotifyVO.setTotalAmount(String.valueOf(order.getTotalAmount()));// 订单总额(单位:元)
        orderPayNotifyVO.setPayTime(DateUtils.format(order.getPayTime(), DateUtils.YYYY_MM_DD_HH_MM_SS));// 支付时间
        orderPayNotifyVO.setAppId(order.getAppId());// 渠道appid
        orderPayNotifyVO.setPaymentCode(order.getPaymentCode());// 支付方式编码
        orderPayNotifyVO.setPayType(String.valueOf(order.getPayType()));// 支付类型(0.在线支付;1.纯签约;2.支付中签约;3.签约扣款)
        orderPayNotifyVO.setOrderStatus(String.valueOf(order.getOrderStatus()));// 订单状态(0.无效;1.有效)
        orderPayNotifyVO.setPayStatus(String.valueOf(order.getPayStatus()));// 订单支付状态(0.未支付;1.已支付;2.部分退款;3.全额退款;4.扣款申请受理成功;5.支付失败)
        orderPayNotifyVO.setPassbackParams(order.getPassbackParams());// 公用回传参数
        orderPayNotifyVO.setPlanId(String.valueOf(order.getPlanId()));// 模板id
        orderPayNotifyVO.setContractDisplayAccount(order.getContractDisplayAccount());// 用户账户展示名称
        orderPayNotifyVO.setPayChannelBusiness(order.getPayChannelBusiness());// 支付渠道商户
        orderPayNotifyVO.setPayScene(order.getPayScene());// 支付场景
        orderPayNotifyVO.setPayUser(order.getPayUser());// 支付用户
        orderPayNotifyVO.setUserMobile(order.getUserMobile());// 用户手机号码
        orderPayNotifyVO.setGoodsName(order.getGoodsName());// 商品名称
        orderPayNotifyVO.setGoodsNum(order.getGoodsNum());// 商品数量
        orderPayNotifyVO.setGoodsPrice(order.getGoodsPrice());// 商品成本价格
        orderPayNotifyVO.setRemark(order.getRemark());// 备注
        orderPayNotifyVO.setHbFqNum(order.getHbFqNum());
        orderPayNotifyVO.setHbFqSellerPercent(order.getHbFqSellerPercent());
        Map<String, String> params = ParamsUtils.toMap(orderPayNotifyVO);// 待签名实体转换成map
        params = ParamsUtils.paramFilter(params, true, "sign", "sign_type");// 去掉空值与签名参数后的新签名参数组
        String content = ParamsUtils.join(params);// 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
        // 请求参数签名
        String sign = RsaUtils.rsaSign(content, privateKey, StandardCharsets.UTF_8);
        // 设置签名参数
        orderPayNotifyVO.setSign(sign);
        String reqJsonStr = JSON.toJSONString(orderPayNotifyVO);
        String reqUrl = order.getNotifyUrl();
        log.info("商户订单号:[{}],支付异步通知商户请求地址:[{}],请求报文:[{}]", orderPayNotifyVO.getOutOrderNo(), reqUrl, reqJsonStr);
        ResponseResult result = HttpClientUtils.post(reqUrl, new PostParam().setContentType(HttpClientConstants.ContentType.APPLICATION_JSON_TEXT).setBody(reqJsonStr));
        log.info("商户订单号:[{}],支付异步通知商户响应报文:[{}]", orderPayNotifyVO.getOutOrderNo(), result.getResult());
    }

    @Data
    public static class Order implements java.io.Serializable {
        private Long id;
        private String orderNo;
        private String outOrderNo;
        private String outAppId;
        private String openId;
        private String channelCode;
        private String appId;
        private String paymentCode;
        private String paymentName;
        private Integer payType;
        private Integer payBusiness;
        private Integer accessType;
        private Integer orderStatus;
        private String returnUrl;
        private String notifyUrl;
        private String subject;
        private String passbackParams;
        private java.math.BigDecimal totalAmount;
        private java.util.Date expireTime;
        private Integer isExpireTime;
        private Integer payStatus;
        private java.util.Date payTime;
        private String payChannelBusiness;
        private String payScene;
        private String payUser;
        private String userMobile;
        private String goodsName;
        private String goodsNum;
        private String goodsPrice;
        private String orderPaymentCode;
        private String contractId;
        private Long planId;
        private String contractDisplayAccount;
        private String contractNotifyUrl;
        private String remark;
        private String creater;
        private java.util.Date createTime;
        private String modifier;
        private java.util.Date modifyTime;
        private Integer isDeleted;
        private java.math.BigDecimal payFeeAmount;
        private java.math.BigDecimal payFee;
        private String installment;
        private String hbFqNum;
        private String hbFqSellerPercent;
    }

    @Data
    public static class OrderPayNotifyVO implements java.io.Serializable {
        private static final long serialVersionUID = 6650217239454466731L;
        private String orderNo;
        private String outOrderNo;
        private String totalAmount;
        private String payTime;
        private String appId;
        private String paymentCode;
        private String payType;
        private String orderStatus;
        private String payStatus;
        private String passbackParams;
        private String planId;
        private String contractDisplayAccount;
        private String payChannelBusiness;
        private String payScene;
        private String payUser;
        private String userMobile;
        private String goodsName;
        private String goodsNum;
        private String goodsPrice;
        private String remark;
        private String hbFqNum;
        private String hbFqSellerPercent;
        private String sign;
    }
}
