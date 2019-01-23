package com.jaf.tcp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;

public class TcpClient {

    static {
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);
    }

    private final Logger logger = LogManager.getLogger();

    private Channel channel;
    private String ip;
    private int port;

    public TcpClient(String s, int port){
        this.ip = ip;
        this.port = port;
    }


    public void start(){
        Bootstrap boss = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Charset charset = Charset.forName("US-ASCII");
        boss.group(workerGroup);
        boss.channel(NioSocketChannel.class);
        boss.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        boss.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LineBasedFrameDecoder(1024));
                pipeline.addLast(new TimeClientHandler());
                pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                pipeline.addLast(new StringDecoder(charset));
                pipeline.addLast(new StringEncoder(charset));


            }
        });
        logger.info("压测客户端启动成功");
        try {
            ChannelFuture f = boss.connect(ip, port).sync();

            channel = f.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }


    public void stop(){
        if(channel != null){
            channel.closeFuture();
        }
    }


}
