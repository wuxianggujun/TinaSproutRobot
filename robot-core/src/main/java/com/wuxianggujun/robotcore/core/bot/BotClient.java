package com.wuxianggujun.robotcore.core.bot;

import com.wuxianggujun.robotcore.core.framework.WebSocketClientInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

public class BotClient {

    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    private Channel channel;
    private Bootstrap bootstrap = null;


    public BotClient() {
        init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        // 主线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup).option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new WebSocketClientInit());
    }

    public void connection() {
        if (channel != null && channel.isActive()) {
            return;
        }
        try {
            URI uri = new URI(BotConfig.URL);
            logger.info(uri.toString());
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


//    public Channel getChannel() {
//        if (channel == null || !channel.isActive() || !channel.pipeline().get(WebSocketClientHandler.class).getWebSocketClientHandshakes().isHandshakeComplete()) {
//            throw new RuntimeException(String.format("[%s]连接失败", "缇娜-斯普朗特"));
//        }
//        return channel;
//    }


}