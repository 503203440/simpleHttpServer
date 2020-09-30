package net.gpai.controller;

import lombok.extern.slf4j.Slf4j;
import net.gpai.util.FileUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/icbcPay")
@RestController
public class Controller {


    @RequestMapping("/b2bPayNotify")
    public String notice(HttpServletRequest request) {
        try {
            //获取所有表单对象 枚举集合
            Enumeration<String> params = request.getParameterNames();
            Map<String, String> paramMap = new HashMap<>();
            while (params.hasMoreElements()) {
                String name = params.nextElement();
                String value = request.getParameter(name);
                String decode = URLDecoder.decode(value, "GBK");
                log.info("name:{}\tvalue:{}\tdecodeValue:{}", name, value, decode);
                paramMap.put(name, decode);
            }
            if (!paramMap.isEmpty()) {
                log.info("paramMap:{}", paramMap);
                String paramsPath = FileUtil.projectPath() + "/params.txt";
                File file = new File(paramsPath);
                if (!file.exists()) file.createNewFile();
                FileUtil.writeString(file, paramMap.toString());
            }

        } catch (UnsupportedEncodingException e) {
            log.error("编码错误！", e);
        } catch (IOException e) {
            log.error("创建文件失败！", e);
        }


        return "访问成功";
    }
}
