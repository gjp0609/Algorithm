package com.onysakura.cloud.common.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.Objects;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Override
    public Server choose(Object key) {
        // 获取配置文件中所配置的集群名称
        String clusterName = discoveryProperties.getClusterName();
        BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        // 获取需要请求的微服务名称
        String serviceId = loadBalancer.getName();

        // 获取服务发现的相关API
        NamingService namingService = nacosServiceManager.getNamingService(discoveryProperties.getNacosProperties());

        try {
            // 获取该微服务的所有健康实例
            List<Instance> instances = namingService.getAllInstances(serviceId, discoveryProperties.getGroup(), true);
            // 过滤出相同集群下的所有实例
            List<Instance> sameClusterInstances = instances.stream()
                    .filter(i -> Objects.equals(i.getClusterName(), clusterName))
                    .collect(Collectors.toList());

            // 相同集群下没有实例则需要使用其他集群下的实例
            List<Instance> instancesToBeChosen;
            if (CollectionUtils.isEmpty(sameClusterInstances)) {
                instancesToBeChosen = instances;
                log.warn("发生跨集群调用，name = {}, clusterName = {}, instances = {}", serviceId, clusterName, instances);
            } else {
                instancesToBeChosen = sameClusterInstances;
            }

            // 基于随机权重的负载均衡算法，从实例列表中选取一个实例
            Instance instance = ExtendBalancer.getHost(instancesToBeChosen);
            log.info("选择的实例是：port = {}, instance = {}", instance.getPort(), instance);

            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("获取实例发生异常", e);
            return null;
        }
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        // do nothing
    }

    static class ExtendBalancer extends Balancer {

        /**
         * 由于Balancer类里的getHostByRandomWeight方法是protected的，
         * 所以通过这种继承的方式来实现调用，该方法基于随机权重的负载均衡算法，选取一个实例
         */
        static Instance getHost(List<Instance> hosts) {
            return getHostByRandomWeight(hosts);
        }
    }
}
