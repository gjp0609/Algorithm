package fun.onysakura.cloud.service.user.c;

import fun.onysakura.cloud.common.properties.ApplicationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping
public class TestController {

    @Autowired
    private ApplicationInfo applicationInfo;

    @GetMapping("/test/path/{ppp}")
    public String path(@PathVariable("ppp") String ppp) {
        System.out.println(ppp);
        return ppp;
    }

    @GetMapping("/test/rp")
    public String rp(@RequestParam("ppp") String ppp) {
        System.out.println(ppp);
        return ppp;
    }

    @GetMapping("info")
    public String info() {

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return time + " " + applicationInfo.applicationName + ": " + applicationInfo.machineId;
    }
}
