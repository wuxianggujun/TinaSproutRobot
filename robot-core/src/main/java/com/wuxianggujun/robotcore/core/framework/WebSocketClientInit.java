package com.wuxianggujun.robotcore.core.framework;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;

public class WebSocketClientInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //添加HttpClientCodeC：将请求和应答消息解码或者解码为Http消息
        pipeline.addLast(new HttpClientCodec());
        //添加HttpObjectAggregator：目的是将Http消息的多个部分组合成一条完整的Http消息
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 100));
        //
        pipeline.addLast(new WebSocketFrameAggregator(1024 * 1024 * 100));
        //添加WebSocket客户端的Handler
        pipeline.addLast(new WebSocketClientHandler());
    }
}