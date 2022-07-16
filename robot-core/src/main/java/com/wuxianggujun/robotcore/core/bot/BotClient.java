package com.wuxianggujun.robotcore.core.bot;

import com.wuxianggujun.robotcore.core.api.ApiResult;
import com.wuxianggujun.robotcore.core.api.BaseApi;
import com.wuxianggujun.robotcore.core.framework.WebSocketClientHandler;
import com.wuxianggujun.robotcore.core.framework.WebSocketClientInit;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class BotClient {

    private static final Logger logger = LoggerFactory.getLogger(BotClient.class);
    private Channel channel;
    private Bootstrap bootstrap = null;

    private final Lock lock = new ReentrantLock();


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
            f.addListener((ChannelFutureListener) channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    final EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(() -> {
                        System.out.println("not connect service");
                        connection();
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    channel = channelFuture.channel();
                    System.out.println("connected");
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public ApiResult invokeApi(BaseApi baseApi) {
        this.lock.lock();
        ApiResult apiResult = null;
        try {
            channel.writeAndFlush(new TextWebSocketFrame(baseApi.buildJson()));
            apiResult = new ApiResult();
            apiResult.setRetCode(200);
            apiResult.setStatus("ok");
            apiResult.setData("Cao");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return apiResult;
    }


    public Channel getChannel() {
        if (channel == null || !channel.isActive() || !channel.pipeline().get(WebSocketClientHandler.class).getWebSocketClientHandshakes().isHandshakeComplete()) {
            throw new RuntimeException(String.format("[%s]连接失败", "缇娜-斯普朗特"));
        }
        return channel;
    }


}