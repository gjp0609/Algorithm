package com.onysakura.cloud.platform.gateway.ss;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.onysakura.cloud.common.constants.StatusOnOff;
import com.onysakura.cloud.platform.gateway.entity.GatewayRoute;
import com.onysakura.cloud.platform.gateway.mapper.GatewayRouteMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RouteService {

    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;
    @Autowired
    private RouteEventService dynamicRouteService;

    @PostConstruct
    public void init() {
        log.info("gateway route init...");
        try {
            List<GatewayRoute> gatewayRoutes = gatewayRouteMapper.selectList(Wrappers.<GatewayRoute>lambdaQuery().eq(GatewayRoute::getStatus, StatusOnOff.ON));
            log.info("获取网关配置: {} 条", gatewayRoutes.size());
            for (GatewayRoute gatewayRoute : gatewayRoutes) {
                log.info("add route : {}", gatewayRoute);
                dynamicRouteService.add(toRouteDefinition(gatewayRoute));
            }
        } catch (Exception e) {
            log.error("初始化网关路由时发生错误", e);
        }
    }

    public void refresh() {
        List<GatewayRoute> gatewayRoutes = gatewayRouteMapper.selectList(Wrappers.<GatewayRoute>lambdaQuery().eq(GatewayRoute::getStatus, StatusOnOff.ON));
        ArrayList<RouteDefinition> routeDefinitions = new ArrayList<>();
        for (GatewayRoute gatewayRoute : gatewayRoutes) {
            RouteDefinition routeDefinition = toRouteDefinition(gatewayRoute);
            routeDefinitions.add(routeDefinition);
        }
        dynamicRouteService.updateList(routeDefinitions);
    }

    private RouteDefinition toRouteDefinition(GatewayRoute gatewayRoute) {
        // definition
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRoute.getRouteId());
        // predicates
        List<PredicateDefinition> predicates = Lists.newArrayList();
        // Path
        PredicateDefinition predicatePath = new PredicateDefinition();
        predicatePath.setName("Path");
        Map<String, String> predicatePathParams = new HashMap<>(8);
        predicatePathParams.put("name", StringUtils.isBlank(gatewayRoute.getRouteName()) ? gatewayRoute.getRouteId().toString() : gatewayRoute.getRouteName());
        predicatePathParams.put("pattern", gatewayRoute.getPath());
        predicatePathParams.put("pathPattern", gatewayRoute.getPath());
        predicatePath.setArgs(predicatePathParams);
        predicates.add(predicatePath);
        definition.setPredicates(predicates);

        List<FilterDefinition> filters = Lists.newArrayList();
        FilterDefinition stripPrefixDefinition = new FilterDefinition();
        Map<String, String> stripPrefixParams = new HashMap<>(8);
        stripPrefixDefinition.setName("StripPrefix");
        stripPrefixParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
        stripPrefixDefinition.setArgs(stripPrefixParams);
        filters.add(stripPrefixDefinition);
        definition.setFilters(filters);
        String uriString = StringUtils.isNotBlank(gatewayRoute.getUrl()) ? gatewayRoute.getUrl() : "lb://" + gatewayRoute.getServiceId();
        URI uri = UriComponentsBuilder.fromUriString(uriString).build().toUri();
        definition.setUri(uri);
        definition.setOrder(gatewayRoute.getRouteOrder());
        return definition;
    }
}
