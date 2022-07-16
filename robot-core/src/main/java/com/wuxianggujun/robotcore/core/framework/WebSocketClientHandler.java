package com.wuxianggujun.robotcore.core.framework;

import com.wuxianggujun.robotcore.core.BotDispatcher;
import com.wuxianggujun.robotcore.core.bot.BotConfig;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketClientHandler.class);

    //这个类主要实现的就是client和server端之间的握手。
    private WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture = null;

    public WebSocketClientHandler() {
       
    }

    public ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }

    public WebSocketClientHandshaker getWebSocketClientHandshakes() {
        return this.handshaker;
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }


    /**
     * 接收服务器传来的消息，进行业务处理
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     */
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

        if (msg instanceof WebSocketFrame){
            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                BotDispatcher.getInstance().handle(textFrame.text());
                logger.info("接收到TXT消息: " + textFrame.text());
            } else if (frame instanceof PongWebSocketFrame) {
                logger.info("接收到pong消息");
            } else if (frame instanceof CloseWebSocketFrame) {
                logger.info("接收到closing消息");
                ch.close();
            }
        }
    
    }

    /**
     * 客户端与服务器断开连接的时候调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        this.handshakeFuture = ctx.newPromise();
    }

    /**
     * 客户端与服务端创建连接的时候调用
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //这里可以添加请求头部信息
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(URI.create(BotConfig.URL), WebSocketVersion.V13, null, true, httpHeaders);
        handshaker.handshake(ctx.channel());
    }

    /**
     * 发生异常
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("WebSocketClientHandler::exceptionCaught");
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.channel().close();
    }

}