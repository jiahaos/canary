package com.jaf.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TimeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int counter;
    private byte[] req;
    private int req_times = 100;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);
        String body = new String(req, "UTF-8");
        System.out.println(
                "Now is : "+ body +" ; the counter is :" + ++counter
        );

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i=0; i<req_times; i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
