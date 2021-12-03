package fun.onysakura.cloud.platform.gateway.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@TableName("CLOUD_GATEWAY_ROUTE")
public class GatewayRoute {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 路由ID
     */
    private String routeId;
    /**
     * 路由名称
     */
    private String routeName;
    /**
     * 路由顺序
     */
    private Integer routeOrder;
    /**
     * 路径
     */
    private String path;
    /**
     * 服务ID
     */
    private String serviceId;
    /**
     * 完整地址
     */
    private String url;
    private Integer status;
}
