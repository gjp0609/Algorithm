package fun.onysakura.cloud.platform.gateway.controller;

import fun.onysakura.cloud.platform.gateway.entity.GatewayRoute;
import fun.onysakura.cloud.platform.gateway.mapper.GatewayRouteMapper;
import fun.onysakura.cloud.platform.gateway.ss.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;
    @Autowired
    private RouteService routeService;

    @GetMapping("/save")
    public void save() {
        GatewayRoute gatewayRoute = new GatewayRoute();
        gatewayRoute.setRouteId("qaweq");
        gatewayRoute.setStatus(1);
        gatewayRouteMapper.insert(gatewayRoute);
    }

    @GetMapping("/refresh")
    public void refresh() {
        routeService.refresh();
    }
}
