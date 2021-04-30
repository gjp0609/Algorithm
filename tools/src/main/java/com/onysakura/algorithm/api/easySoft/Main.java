package com.onysakura.algorithm.api.easySoft;

import com.alibaba.fastjson.JSON;
import com.onysakura.algorithm.utilities.basic.XmlUtils;

public class Main {

    public static void main(String[] args) throws Exception {
        String userPhoneNum = "-";
        String ry = "-"; // 易软的资源ID
        String srcYrProjectId = "-"; // 易软的项目ID
        if (true) {
            String e656 = Functions.E656(srcYrProjectId, ry, "2010-01", "2021-04");
            System.out.println(JSON.toJSONString(XmlUtils.xmlToMap(e656), true));
        }
        if (false) {
            String e657 = Functions.E657(srcYrProjectId, ry, "2010-01", "2021-04");
            System.out.println(JSON.toJSONString(XmlUtils.xmlToMap(e657), true));
        }
        if (false) {
            String s = Functions.GXAPP_E839("", "", "", "");
            System.out.println(s);
            System.out.println(JSON.toJSONString(XmlUtils.xmlToMap(s), true));
        }
    }
}
