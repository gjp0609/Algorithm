package com.onysakura.cloud.service.manager.service;

import com.onysakura.cloud.service.manager.feign.TestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestFeign testFeign;

    public String path(String ppp) {
        return testFeign.path(ppp);
    }

    public String rp(String ppp) {
        return testFeign.rp(ppp);
    }

}
