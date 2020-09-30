package net.gpai.server;

import lombok.extern.slf4j.Slf4j;
import net.gpai.util.FileUtil;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 监听端口的服务
 */
@Slf4j
public class Service {

    public static final String projectPath = FileUtil.projectPath();

    /**
     * @param args
     */
    public static void main(String[] args) {
        Service s = new Service();
        try {

            if (args == null || args.length == 0) {
                log.error("请使用端口号作为第一个参数启动程序：java -jar xxx.jar {port}");
                return;
            }
            int port;
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                log.error("端口号不正确>{}", e.getMessage());
                return;
            }
            s.startService(port);
        } catch (IOException e) {
            log.error("启动服务异常", e);
        }
    }


    public void startService(int port) throws IOException {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("启动成功，使用端口：{}", port);
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("收到请求");
                Thread thread = new Task(socket);
                thread.start();
            }
        } catch (BindException e) {
            log.error("端口被占用！{}", e.getMessage());
            return;
        }

    }

    public static void consumer(Socket socket) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int available = inputStream.available();
            byte[] bytes = new byte[available*2];
            inputStream.read(bytes);
            byteArrayOutputStream.write(bytes, 0, available);

            int size = byteArrayOutputStream.size();
            String gbk = byteArrayOutputStream.toString("GBK");
            String utf8 = byteArrayOutputStream.toString("UTF-8");
            String iso88591 = byteArrayOutputStream.toString("ISO-8859-1");
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            StringBuffer sb = new StringBuffer();
            for (byte b : byteArray) {
                sb.append(b).append(",");
            }

            String byteContent = sb.toString();

            String byteFile = projectPath + "/byteFile.txt";

            File file = new File(byteFile);
            file.createNewFile();

            FileUtil.writeString(file, byteContent);

            outputStream = socket.getOutputStream();
            String responseHttp = responseHttp();
            outputStream.write(responseHttp.getBytes("UTF-8"));
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log.error("inputStream.close is error", e);
            }
            log.info("处理完成！");
        }

    }


    public static String responseHttp() {
        String content = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html;charset=utf-8\r\n" +
                "Server: gpaiServer/1.0\r\n" +
                "\r\n<h1>welcome！</h1>";
        return content;
    }


}

class Task extends Thread {

    private Socket socket;

    public Task(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Service.consumer(socket);
    }
}
