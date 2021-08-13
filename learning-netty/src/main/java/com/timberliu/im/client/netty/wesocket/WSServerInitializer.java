package com.timberliu.im.client.netty.wesocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by liujie on 2021/7/12
 */

public class WSServerInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        // websocket 基于 http 协议，需要要 http 编解码器
        ch.pipeline().addLast(new HttpServerCodec());
        // 对写大数据流的支持
        ch.pipeline().addLast(new ChunkedWriteHandler());
        // http 消息聚合，聚合成 FullHttRequest 或 FullHttpResponse
        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 60));

        // ========== 以上用于支持 http 协议 ==================
        /*
         * websocket 服务，可以指定客户端连接访问的路由
         * 该 handler 处理握手/心跳/关闭
         */
        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));

        ch.pipeline().addLast(new ChatHandler());

    }
}
