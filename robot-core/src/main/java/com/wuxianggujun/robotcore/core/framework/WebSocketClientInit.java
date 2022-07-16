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
        //字符串编码解码
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 100));
        pipeline.addLast(new WebSocketFrameAggregator(1024 * 1024 * 100));
        //客户端的逻辑
        pipeline.addLast(new WebSocketClientHandler());
    }
}