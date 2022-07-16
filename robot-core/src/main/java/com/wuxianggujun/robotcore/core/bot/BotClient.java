package com.wuxianggujun.robotcore.core.bot;

import com.wuxianggujun.robotcore.core.framework.WebSocketClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class BotClient {
    private Channel channel;
    private Bootstrap bootstrap = null;

    private final String url;

  
    public BotClient(String url) {
        this.url = url;
        init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        // 主线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup).option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,
                                Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes())));

                        //字符串编码解码
                        pipeline.addLast(new HttpClientCodec());
                        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 100));
                        pipeline.addLast(new WebSocketFrameAggregator(1024 * 1024 * 100));
                        //心跳检测
                        pipeline.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
                        //客户端的逻辑
                        pipeline.addLast(new WebSocketClientHandler());

                    }
                });
    }

    public void connection() {
        if (channel != null && channel.isActive()) {
            return;
        }
        try {
            URI uri = new URI(url);
            ChannelFuture f = bootstrap.connect(uri.getHost(), uri.getPort());
            //断线重连
            f.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        final EventLoop loop = channelFuture.channel().eventLoop();
                        loop.schedule(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("not connect service");
                                connection();
                            }
                        }, 1L, TimeUnit.SECONDS);
                    } else {
                        channel = channelFuture.channel();
                        System.out.println("connected");
                    }
                }
            });


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public Channel getChannel() {
        if (channel == null || !channel.isActive() || !channel.pipeline().get(WebSocketClientHandler.class).getWebSocketClientHandshakes().isHandshakeComplete()) {
            throw new RuntimeException(String.format("[%s]连接失败", "缇娜-斯普朗特"));
        }
        return channel;
    }


}