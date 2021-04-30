package com.onysakura.algorithm.api.easySoft;

import com.onysakura.algorithm.utilities.basic.str.RandomUtils;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientConstants;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientUtils;
import com.onysakura.algorithm.utilities.web.httpclient.PostParam;
import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

public class Functions {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final String ESB_DEFAULT_SOAP_XMLNS_PREFIX = "http://www.ekingwin.com/esb";
    private static final String ESB_TEST_DOMAIN = "http://192.168.2.219:8001";
    private static final String ESB_DOMAIN = ESB_TEST_DOMAIN;

    /**
     * 3.1 查询该资源下欠费总额
     */
    public static String E656(String srcYrProjectId, String ry, String beginMonth, String endMonth) throws Exception {
        String soapXmlns = ESB_DEFAULT_SOAP_XMLNS_PREFIX + "/GXAPP_E656_EsObjectToPaymentForm";
        String path = "/WP_SUNAC/APP_GXAPP_SERVICES/Proxy_Services/TA_ES/GXAPP_E656_EsObjectToPaymentForm_PS?wsdl";
        String webserviceUrl = ESB_DOMAIN + path;
        String SRC_SYS = "BS-GXAPP-Q";
        String TAR_SYS = "BS-YRS-Q";
        String SYN_FLAG = "S";
        String COUNT = "1";
        String SERVER_NAME = "GXAPP_E656_EsObjectToPaymentForm";
        String BIZ_TRANSACTION_ID = "GXAPP_E656_" + seventeenTimeStamp() + "_" + RandomUtils.randomNumber(4);

        String str = "";
        str += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">";
        str += "  <soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> </soap:Header>";
        str += "  <soapenv:Body>";
        str += "    <gxap:" + SERVER_NAME + " xmlns:gxap=\"" + soapXmlns + "\">";
        str += "      <I_REQUEST>";
        str += "        <REQ_BASEINFO>";
        str += "          <REQ_TRACE_ID>" + uuid() + "</REQ_TRACE_ID>";
        str += "          <REQ_SEND_TIME>" + fifteenTimeStr() + "</REQ_SEND_TIME>";
        str += "          <REQ_SRC_SYS>" + SRC_SYS + "</REQ_SRC_SYS>";
        str += "          <REQ_TAR_SYS>" + TAR_SYS + "</REQ_TAR_SYS>";
        str += "          <REQ_SERVER_NAME>" + SERVER_NAME + "</REQ_SERVER_NAME>";
        str += "          <REQ_SYN_FLAG>" + SYN_FLAG + "</REQ_SYN_FLAG>";
        str += "          <REQ_BSN_ID/>";
        str += "          <REQ_RETRY_TIMES/>";
        str += "          <REQ_REPEAT_FLAG/>";
        str += "          <REQ_REPEAT_CYCLE/>";
        str += "          <BIZTRANSACTIONID>" + BIZ_TRANSACTION_ID + "</BIZTRANSACTIONID>";
        str += "          <COUNT>" + COUNT + "</COUNT>";
        str += "        </REQ_BASEINFO>";
        str += "        <MESSAGE>";
        str += "          <REQ_ITEM>";
        str += "            <AREAID>" + srcYrProjectId + "</AREAID>";       // 项目(Id)
        str += "            <OBJECTID>" + ry + "</OBJECTID>";  // 资源(Id)
        str += "            <BEGINMONTH>" + beginMonth + "</BEGINMONTH>";
        str += "            <ENDMONTH>" + endMonth + "</ENDMONTH>";
        str += "          </REQ_ITEM>";
        str += "        </MESSAGE>";
        str += "      </I_REQUEST>";
        str += "    </gxap:" + SERVER_NAME + ">";
        str += "  </soapenv:Body>";
        str += "</soapenv:Envelope>";

        ResponseResult result = HttpClientUtils.post(webserviceUrl,
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.WEBSERVICE_XML_TEXT)
                        .setBody(str)
        );
        return result.getResult();
    }

    /**
     * 3.2 查询该资源下欠费总额
     */
    public static String E657(String srcYrProjectId, String ry, String beginMonth, String endMonth) throws Exception {
        String soapXmlns = ESB_DEFAULT_SOAP_XMLNS_PREFIX + "/GXAPP_E657_QueryTotalOfResource";
        String path = "/WP_SUNAC/APP_GXAPP_SERVICES/Proxy_Services/TA_ES/GXAPP_E657_QueryTotalOfResource_PS?wsdl";
        String webserviceUrl = ESB_DOMAIN + path;
        String SRC_SYS = "BS-GXAPP-Q";
        String TAR_SYS = "BS-YRS-Q";
        String SYN_FLAG = "S";
        String COUNT = "1";
        String SERVER_NAME = "GXAPP_E657_QueryTotalOfResource";
        String BIZ_TRANSACTION_ID = "GXAPP_E657_" + seventeenTimeStamp() + "_" + RandomUtils.randomNumber(4);

        String str = "";
        str += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gxap=\"" + soapXmlns + "\">";
        str += "   <soapenv:Header/>";
        str += "   <soapenv:Body>";
        str += "      <gxap:" + SERVER_NAME + ">";
        str += "         <I_REQUEST>";
        str += "            <REQ_BASEINFO>";
        str += "               <REQ_TRACE_ID>" + uuid() + "</REQ_TRACE_ID>";
        str += "               <REQ_SEND_TIME>" + fifteenTimeStr() + "</REQ_SEND_TIME>";
        str += "               <REQ_SRC_SYS>" + SRC_SYS + "</REQ_SRC_SYS>";
        str += "               <REQ_TAR_SYS>" + TAR_SYS + "</REQ_TAR_SYS>";
        str += "               <REQ_SERVER_NAME>" + SERVER_NAME + "</REQ_SERVER_NAME>";
        str += "               <REQ_SYN_FLAG>" + SYN_FLAG + "</REQ_SYN_FLAG>";
        str += "               <REQ_BSN_ID></REQ_BSN_ID>";
        str += "               <REQ_RETRY_TIMES></REQ_RETRY_TIMES>";
        str += "               <REQ_REPEAT_FLAG></REQ_REPEAT_FLAG>";
        str += "               <REQ_REPEAT_CYCLE></REQ_REPEAT_CYCLE>";
        str += "               <BIZTRANSACTIONID>" + BIZ_TRANSACTION_ID + "</BIZTRANSACTIONID>";
        str += "               <COUNT>" + COUNT + "</COUNT>";
        str += "            </REQ_BASEINFO>";
        str += "            <MESSAGE>";
        str += "               <REQ_ITEM>";
        str += "                  <AREAID>" + srcYrProjectId + "</AREAID>";          // 项目(Id)
        str += "                  <OBJECTID>" + ry + "</OBJECTID>";     // 资源(Id)
        str += "                  <BEGINMONTH>" + beginMonth + "</BEGINMONTH>";
        str += "                  <ENDMONTH>" + endMonth + "</ENDMONTH>";
        str += "               </REQ_ITEM>";
        str += "            </MESSAGE>";
        str += "			</I_REQUEST>";
        str += "      </gxap:" + SERVER_NAME + ">";
        str += "   </soapenv:Body>";
        str += "</soapenv:Envelope>";

        ResponseResult result = HttpClientUtils.post(webserviceUrl,
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.WEBSERVICE_XML_TEXT)
                        .setBody(str)
        );
        return result.getResult();
    }

    /**
     * 3.2 查询该资源下欠费总额
     */
    public static String GXAPP_E839(String srcYrProjectId, String ry, String beginMonth, String endMonth) throws Exception {
        String username = "ESB_Q_GXAPP";
        String password = "gxapptest@esb";
        String soapXmlns = ESB_DEFAULT_SOAP_XMLNS_PREFIX + "/GXAPP_E839_PrepayDiscountDeductionAccount";
        String path = "/WP_SUNAC/APP_GXAPP_SERVICES/Proxy_Services/TA_YRS/GXAPP_E839_PrepayDiscountDeductionAccount_PS?wsdl";
        String webserviceUrl = ESB_DOMAIN + path;
        String SRC_SYS = "BS-GXAPP-Q";
        String TAR_SYS = "BS-YRS-Q";
        String SYN_FLAG = "S";
        String COUNT = "1";
        String SERVER_NAME = "GXAPP_E839_PrepayDiscountDeductionAccount";
        String BIZ_TRANSACTION_ID = "GXAPP_E839" + seventeenTimeStamp() + "_" + RandomUtils.randomNumber(4);

        String items = "";
        items += "<GXAPP_OFFERSFEE>";
        items += "    <ACTTYPE></ACTTYPE>"; // 预存账户类型
        items += "    <PAYABLEAMT></PAYABLEAMT>"; // 应缴金额
        items += "    <PAIDAMT></PAIDAMT>"; // 实缴金额
        items += "    <OFFERSAMT></OFFERSAMT>"; // 优惠金额
        items += "    <SUBJECTID></SUBJECTID>"; // 周期科目id
        items += "    <PAYDATE></PAYDATE>"; // 科目到期时间
        items += "    <OFFERSRATIOS></OFFERSRATIOS>"; // 优惠比率
        items += "    <OFFERSCOUPONS></OFFERSCOUPONS>"; // 优惠抵扣券 
        items += "    <OFFERSREASONS></OFFERSREASONS>"; // 优惠原因
        items += "</GXAPP_OFFERSFEE>";

        String str = "";
        str += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gxap=\"" + soapXmlns + "\">";
        str += "   <soapenv:Header/>";
        str += "   <soapenv:Body>";
        str += "      <gxap:" + SERVER_NAME + ">";
        str += "         <I_REQUEST>";
        str += "            <REQ_BASEINFO>";
        str += "               <REQ_TRACE_ID>" + uuid() + "</REQ_TRACE_ID>";
        str += "               <REQ_SEND_TIME>" + fifteenTimeStr() + "</REQ_SEND_TIME>";
        str += "               <REQ_SRC_SYS>" + SRC_SYS + "</REQ_SRC_SYS>";
        str += "               <REQ_TAR_SYS>" + TAR_SYS + "</REQ_TAR_SYS>";
        str += "               <REQ_SERVER_NAME>" + SERVER_NAME + "</REQ_SERVER_NAME>";
        str += "               <REQ_SYN_FLAG>" + SYN_FLAG + "</REQ_SYN_FLAG>";
        str += "               <REQ_BSN_ID></REQ_BSN_ID>";
        str += "               <REQ_RETRY_TIMES></REQ_RETRY_TIMES>";
        str += "               <REQ_REPEAT_FLAG></REQ_REPEAT_FLAG>";
        str += "               <REQ_REPEAT_CYCLE></REQ_REPEAT_CYCLE>";
        str += "               <BIZTRANSACTIONID>" + BIZ_TRANSACTION_ID + "</BIZTRANSACTIONID>";
        str += "               <COUNT>" + COUNT + "</COUNT>";
        str += "            </REQ_BASEINFO>";
        str += "            <MESSAGE>";
        str += "               <REQ_ITEM>";
        str += "                  <AREAID>" + srcYrProjectId + "</AREAID>";          // 项目(Id)
        str += "                  <OBJECTID>" + ry + "</OBJECTID>";     // 资源(Id)
        str += "                  <CUSTID>" + ry + "</CUSTID>";
        str += "                  <PAID_TOTALAMT>" + ry + "</PAID_TOTALAMT>"; // 实缴预存总金额
        str += "                  <PAYABLE_TOTALAMT>" + ry + "</PAYABLE_TOTALAMT>"; // 应缴预存总金额
        str += "                  <GXAPP_OFFERSFEEITEM>" + items + "</GXAPP_OFFERSFEEITEM>"; // 费用数组
        str += "                  <ORDERNO>" + "11111" + "</ORDERNO>"; // 银行订单号
        str += "                  <SERIALNO>" + "11111" + "</SERIALNO>"; // 银行流水号
        str += "                  <CUSTMOBILE>" + "11111" + "</CUSTMOBILE>"; // 缴费人手机号
        str += "                  <SOURCE>" + "1" + "</SOURCE>"; // 来源
        str += "                  <BANKPAYTYPE>" + "1" + "</BANKPAYTYPE>"; // 银行支付方式
        str += "                  <OFFERS_TOTALAMT>" + "1" + "</OFFERS_TOTALAMT>"; // 优惠总金额
        str += "                  <OFFERS_REMARKS>" + "" + "</OFFERS_REMARKS>"; // 优惠备注
        str += "                  <FIELD1>" + "" + "</FIELD1>"; // 预留字段
        str += "                  <FIELD2>" + "" + "</FIELD2>"; // 预留字段
        str += "               </REQ_ITEM>";
        str += "            </MESSAGE>";
        str += "			</I_REQUEST>";
        str += "      </gxap:" + SERVER_NAME + ">";
        str += "   </soapenv:Body>";
        str += "</soapenv:Envelope>";

        ResponseResult result = HttpClientUtils.post(webserviceUrl,
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.WEBSERVICE_XML_TEXT)
                        .setBody(str)
                        .addHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString((username + ":" + password).getBytes()))
        );
        return result.getResult();
    }


    /**
     * 当前时间十五位时间串
     */
    private static String fifteenTimeStr() {
        return String.valueOf(Long.parseLong(NOW.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))) / 100);
    }

    /**
     * 十七位时间戳
     */
    private static String seventeenTimeStamp() {
        return NOW.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
