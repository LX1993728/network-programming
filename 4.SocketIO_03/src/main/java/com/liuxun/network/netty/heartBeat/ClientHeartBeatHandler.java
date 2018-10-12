package com.liuxun.network.netty.heartBeat;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/**
 * @apiNote
 */
public class ClientHeartBeatHandler extends ChannelHandlerAdapter {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private ScheduledFuture<?> heartBeat;
    // 主动向服务器发送认证信息
    private InetAddress addr;

    private  static final String SUCCESS_KEY = "auth_success_key";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        String key = "1234";
        // 证书
        String auth = ip+","+key;
        ctx.writeAndFlush(auth);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }
}
