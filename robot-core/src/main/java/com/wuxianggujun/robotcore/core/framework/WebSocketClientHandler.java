package com.wuxianggujun.robotcore.core.framework;

import com.wuxianggujun.robotcore.core.bot.BotConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
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


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
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

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            logger.info("接收到TXT消息: " + textFrame.text());
        } else if (frame instanceof PongWebSocketFrame) {
            logger.info("接收到pong消息");
        } else if (frame instanceof CloseWebSocketFrame) {
            logger.info("接收到closing消息");
            ch.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        this.handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("WebSocketClientHandler::exceptionCaught");
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.channel().close();
    }

}