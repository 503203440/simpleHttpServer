package net.gpai.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import net.gpai.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static final String projectPath = FileUtil.projectPath();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {

        // 获取请求体的内容
        ByteBuf byteBuf = req.content();
        if (byteBuf.hasArray()) {
            byte[] bytes = byteBuf.array();
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                sb.append(b).append(",");
            }
            String byteContent = sb.toString();
            String byteFile = projectPath + "/byteFile.txt";
            File file = new File(byteFile);
            if (!file.exists()) file.createNewFile();
            FileUtil.writeString(file, byteContent);
        } else {
            int length = byteBuf.readableBytes();
            byte[] bytes = new byte[length];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            StringBuffer sb = new StringBuffer();
            for (byte b : bytes) {
                sb.append(b).append(",");
            }
            String byteContent = sb.toString();
            String byteFile = projectPath + "/byteFile.txt";
            File file = new File(byteFile);
            if (!file.exists()) file.createNewFile();
            FileUtil.writeString(file, byteContent);
        }

        Map<String, Object> requestParams = getRequestParams(req);
        log.info("requestParams:{}", requestParams);

        //100 Continue
        if (HttpUtil.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.CONTINUE));
        }
        // 获取请求的uri
        String uri = req.uri();
        Map<String, String> resMap = new HashMap<>();
        resMap.put("method", req.method().name());
        resMap.put("uri", uri);
        String msg = "<html><head><title>test</title></head><body>你请求uri为：" + uri + "</body></html>";
        // 创建http响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        // 设置头信息
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
        // 将html write到客户端
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Map<String, Object> getRequestParams(FullHttpRequest request) {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        Map<String, Object> params = new HashMap<>();

        for (InterfaceHttpData data : httpPostData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }
}
