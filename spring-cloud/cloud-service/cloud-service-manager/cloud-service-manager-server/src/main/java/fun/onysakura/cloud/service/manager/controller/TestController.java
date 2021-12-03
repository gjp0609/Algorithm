package fun.onysakura.cloud.service.manager.controller;

import fun.onysakura.cloud.service.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/path/{ppp}")
    public String path(@PathVariable("ppp") String ppp) {
        return testService.path(ppp);
    }

    @GetMapping("/rp")
    public String rp(@RequestParam("ppp") String ppp) {
        return testService.rp(ppp);
    }
}
