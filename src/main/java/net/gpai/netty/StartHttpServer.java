package net.gpai.netty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartHttpServer {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            log.error("请输入使用的端口，例如：java -jar xxx.jar 8080");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            log.error("端口号不正确：{}", e.getMessage());
            return;
        }
        HttpServer httpServer = new HttpServer(port);
        httpServer.start();
    }
}
