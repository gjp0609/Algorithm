package com.onysakura.algorithm.api.epay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.onysakura.algorithm.file.image.AwtShowImage;
import com.onysakura.algorithm.utilities.file.FileUtils;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientConstants;
import com.onysakura.algorithm.utilities.web.httpclient.HttpClientUtils;
import com.onysakura.algorithm.utilities.web.httpclient.PostParam;
import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;

import java.util.List;
import java.util.Scanner;

public class ManagerFunctions {

    private static final String URL = "-";

    public static String validateCodeToken() throws Exception {
        String s = HttpClientUtils.get(URL + "/validateCodeToken").getResult();
        JSONObject jsonObject = JSON.parseObject(s);
        if ("10000".equals(jsonObject.getString("code"))) {
            return jsonObject.getJSONObject("data").getString("validateCodeToken");
        } else {
            throw new RuntimeException("validateCodeToken error");
        }
    }

    public static ResponseResult getChannel(String authorization) throws Exception {
        return HttpClientUtils.post(URL + "/channel/getPageChannelList",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_JSON)
                        .addHeader("Authorization", authorization)
                        .addBody("page", "1")
                        .addBody("rows", "50")
        );
    }

    public static String validateCode(String token) throws Exception {
        String s = HttpClientUtils.get(URL + "/validateCode/" + token).getResult();
        JSONObject jsonObject = JSON.parseObject(s);
        if ("10000".equals(jsonObject.getString("code"))) {
            return jsonObject.getJSONObject("data").getString("validateCode");
        } else {
            throw new RuntimeException("validateCodeToken error");
        }
    }

    public static String login(String token, String code, String username, String password) throws Exception {
        ResponseResult result = HttpClientUtils.post(URL + "/login",
                new PostParam()
                        .setContentType(HttpClientConstants.ContentType.APPLICATION_JSON)
                        .addBody("account", username)
                        .addBody("password", password)
                        .addBody("validateCode", code)
                        .addBody("validateCodeToken", token)
        );
        JSONObject jsonObject = JSON.parseObject(result.getResult());
        if (result.isSuccess() && "10000".equals(jsonObject.getString("code"))) {
            List<String> authorization = result.getHeaderFields().get("Authorization");
            return authorization.get(0);
        } else {
            throw new RuntimeException("login error");
        }
    }

    /**
     * 人眼识别
     */
    public static String getCode(String codeImage) throws Exception {
        String codePath = "/Files/Temp/code.jpg";
        FileUtils.base64ImageToFile(codeImage, codePath);
        // AsciiImage.printImage(codePath, 30, 0, false);
        // FileUtils.openFile(codePath);
        AwtShowImage.show(codePath);
        System.out.print("Input validate code: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void uploadImage(String authorization, List<String> uploadFilePaths) throws Exception {
        for (String path : uploadFilePaths) {
            HttpClientUtils.post(URL + "/basic/uploadFileImager",
                    new PostParam()
                            .setContentType(HttpClientConstants.ContentType.MULTIPART_FORM_DATA)
                            .addHeader("Authorization", authorization)
                            .addUploadFilePath(path)
                            .addUploadFileName("photoFile")
            );
        }
    }
}
