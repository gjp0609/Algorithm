package fun.onysakura.algorithm.spring.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/test")
public class TestController {

    Logger log = LoggerFactory.getLogger(TestController.class);

    @PostMapping(value = "/xml", consumes = "application/xml;charset=utf-8")
    public void xml(@RequestBody Object o) {
        System.out.println(o);
    }

    @GetMapping("/testThread")
    public Long testThread(@RequestParam("time") long time) throws Exception {
        log.debug("time: {}", (System.currentTimeMillis() - time));
        Thread.sleep(1500);
        return System.currentTimeMillis();
    }

    @GetMapping("/get")
    public HashMap<String, Object> get(String param) {
        System.out.println(param);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("method", "GET");
        hashMap.put("param", param);
        return hashMap;
    }

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void get(@RequestBody MultiValueMap<String, String> param, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String s : parameterMap.keySet()) {
            System.out.println(s + "-> " + request.getParameter(s));
        }

        System.out.println(sortCheckSignParams(convertMap(param)));
        System.out.println(param);
        System.out.println(param.get("asdsss"));
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("method", "POST");
        hashMap.put("param", param);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        return hashMap;
    }

    private Map<String, String> convertMap(MultiValueMap<String, String> param) {
        HashMap<String, String> map = new HashMap<>();
        for (String s : param.keySet()) {
            map.put(s, param.getFirst(s));
        }
        return map;
    }

    /**
     * 参数组装
     */
    public static String sortCheckSignParams(Map<String, String> params) {
        TreeMap<String, String> treeMap = new TreeMap<>(params);
        String sign = treeMap.remove("sign");
        String signType = treeMap.remove("signType");
        ArrayList<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            list.add(entry.getKey() + "=" + entry.getValue());
        }
        list.add("signType=" + signType);
        list.add("sign=" + sign);
        return String.join("&", list);
    }
}
