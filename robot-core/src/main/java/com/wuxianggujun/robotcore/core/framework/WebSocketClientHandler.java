package com.wuxianggujun.robotcore.core.framework;

import com.wuxianggujun.robotcore.core.bot.BotConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    //这个类主要实现的就是client和server端之间的握手。
    private WebSocketClientHandshaker handshaker = null;
    private ChannelPromise handshakeFuture = null;

    public WebSocketClientHandler() {
      handshaker = WebSocketClientHandshakerFactory.newHandshaker(URI.create(BotConfig.URL), WebSocketVersion.V13, null, true, new DefaultHttpHeaders());

    }

    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                FullHttpResponse response = (FullHttpResponse) msg;
                this.handshaker.finishHandshake(ctx.channel(), response);
                logger.info("websocket Handshake 完成!");
                this.handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                logger.info("websocket连接失败!");
                handshakeFuture.setFailure(e);
            }
            return;
        }


        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) msg;
            System.out.println("Text:" + textWebSocketFrame);
        }
        if (msg instanceof CloseWebSocketFrame) {
            System.out.println("WebSocketClientHandler::CloseWebSocketFrame");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("WebSocketClientHandler::exceptionCaught");
        cause.printStackTrace();
        ctx.channel().close();
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }

}