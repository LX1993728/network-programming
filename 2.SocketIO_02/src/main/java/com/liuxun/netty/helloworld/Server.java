package com.liuxun.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    public static void main(String[] args) throws Exception{
        // 1. 第一个线程组是用于接收client端连接的
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 2. 第二个线程组是用于实际的业务处理操作的
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // 3. 启动NIO服务的辅助启动类, 就是对我们的Server进行一系列配置
        ServerBootstrap b = new ServerBootstrap();
         // 把两个工作线程组加入进来
        b.group(bossGroup, workerGroup)
         // 指定使用 NioServerSocketChannel 这种类型的通道
         .channel(NioServerSocketChannel.class)
         // 一定要使用ChildHandler去绑定具体的事件处理器
         .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel socketChannel) throws Exception {
                 // 可以按照指定的顺序添加多个事件处理器
                socketChannel.pipeline().addLast(new ServerHandler());
             }
         })
          /*
            服务器端TCP内核模块维护有2个队列，我们称之为A,B。
            客户端向服务器端connect的时候，会发送带有SYN标志的包  (第一次握手)。
            服务器端收到客户端发来的SYN标志时，向客户端发送SYN ACK确认 (第二次握手)。
            此时TCP内核模块把客户端连接加入到A队列中，然后服务器收到客户端发来的ACK时 (第三次握手)。
            TCP内核模块把客户端连接从A队列移到B队列，连接完成，应用程序的accept会返回。
            也就是说accept从B队列中取出三次握手的连接。
            A队列和B队列的长度之和是backlog。当A,B队列长度之和大于backlog时，新连接将会被TCP内核拒绝。
            所以如果backlog过小，可能会出现accept速度跟不上, A B队列满了，导致新的客户端无法连接。
            要注意的是: backlog对程序支持的连接数并无影响, backlog影响的只是还没有被accept取出的连接
           */
         .option(ChannelOption.SO_BACKLOG,128) // 设置TCP缓冲区
         //.childOption(ChannelOption.SO_KEEPALIVE,true); // 如果程序比较复杂又子通道的时候，使用childOption方法设置子通道参数
         .option(ChannelOption.SO_KEEPALIVE,true) //  这里只有一个NioServerSocketChannel 使用option方法设置参数即可
         .option(ChannelOption.SO_SNDBUF,32 * 1024) // 设定发送缓冲区大小
         .option(ChannelOption.SO_RCVBUF, 32 * 1024);  // 设定接收缓冲区大小
        // 绑定指定的端口进行监听，NIO
        final ChannelFuture future = b.bind(8765).sync();

        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
