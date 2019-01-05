package com.jaf.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public void start(String ip, int port){
        Bootstrap boss = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        boss.group(workerGroup);
        boss.channel(NioSocketChannel.class);
        boss.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        boss.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new TimeClientHandler());
            }
        });
        try {
            ChannelFuture f = boss.connect(ip, port).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new TimeClient().start("127.0.0.1", 8990);

    }
}
