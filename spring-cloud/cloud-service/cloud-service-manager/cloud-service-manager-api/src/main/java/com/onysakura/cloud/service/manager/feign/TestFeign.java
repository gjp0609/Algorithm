package com.onysakura.cloud.service.manager.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cloud-service-user")
public interface TestFeign {

    @GetMapping("/test/path/{ppp}")
    String path(@PathVariable("ppp") String ppp);

    @GetMapping("/test/rp")
    String rp(@RequestParam("ppp") String ppp);
}
