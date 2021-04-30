package com.onysakura.algorithm.api.epay;

import java.util.Arrays;
import java.util.List;

public class ManagerFileUpload {

    public static void main(String[] args) throws Exception {
        String path = "/Files/Temp/Types/";
        List<String> uploadFilePaths = Arrays.asList(
                path + "image.jpg",
                path + "text.php",
                path + "execute.exe"
        );
        String token = ManagerFunctions.validateCodeToken();
        String codeImage = ManagerFunctions.validateCode(token);
        String code = ManagerFunctions.getCode(codeImage);
        String authorization = ManagerFunctions.login(token, code, "vvvv", "vvvvvv");
        ManagerFunctions.uploadImage(authorization, uploadFilePaths);
    }
}
