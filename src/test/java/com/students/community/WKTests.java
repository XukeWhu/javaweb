package com.students.community;

import java.io.IOException;

public class WKTests {

    public static void main(String[] args){
        String cmd = "D:/setting/wkhtmltopdf/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com d:/setting/data/wk-images/3.png";
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
