package com.onysakura.algorithm.api.epay;

import com.onysakura.algorithm.utilities.web.httpclient.ResponseResult;

public class ManagerChannel {

    public static void main(String[] args) throws Exception {
        String username = "-";
        String password = "-";
        String token = ManagerFunctions.validateCodeToken();
        String codeImage = ManagerFunctions.validateCode(token);
        String code = ManagerFunctions.getCode(codeImage);
        String login = ManagerFunctions.login(token, code, username, password);
        ResponseResult resp = ManagerFunctions.getChannel(login);
        System.out.println(resp);
    }
}
