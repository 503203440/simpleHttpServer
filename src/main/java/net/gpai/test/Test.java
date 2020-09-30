package net.gpai.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String decode = URLDecoder.decode("%D3%D0%B2%D6%B4%A9%A3%A8%B1%DA%BA%CD%A3%A9%CE%A4%CE%DC%C2%C4%B3%AE%CE%BA%CA%E4%D2%C6%C2%C8","GBK");

        System.out.println(decode);
    }


}
