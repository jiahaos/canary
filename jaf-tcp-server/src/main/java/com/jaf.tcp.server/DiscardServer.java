package com.jaf.tcp.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {


    private  ChannelFuture channelFuture;

    private int port;

    public DiscardServer(int port){
        this.port = port;
    }

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap sboot = new ServerBootstrap();
        sboot.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new EchoServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 120)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        try {
            channelFuture = sboot.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8990;
        if(args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new DiscardServer(port).start();

    }
}
