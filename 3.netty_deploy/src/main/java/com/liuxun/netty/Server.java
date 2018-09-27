package com.liuxun.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] args) throws Exception{
        // 1.第一个线程组 是用于接收Client连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 2.第二个线程组 是用于实际的业务处理操作的
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 3.创建一个辅助类Bootstrap, 就是对我们的Server进行一系列的配置
        ServerBootstrap b = new ServerBootstrap();
        // 将两个工作线程组加入进来
        b.group(bossGroup,workerGroup)
         .channel(NioServerSocketChannel.class) // 服务器端需指定NioServerSocketChannel
         .childHandler(new ChannelInitializer<SocketChannel>() { // 这里必须使用childChannel
             @Override
             protected void initChannel(SocketChannel sc) throws Exception {
                sc.pipeline().addLast(new ServerHandler());
             }
         });

        // 绑定指定的端口 进行监听
        ChannelFuture f = b.bind(8765).sync();
        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }

}
