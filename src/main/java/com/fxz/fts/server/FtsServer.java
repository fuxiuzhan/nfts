/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.server 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:44:05 
 * 
 */
package com.fxz.fts.server;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import com.fxz.auth.config.AuthConfig;
import com.fxz.auth.handler.AuthHandler;
import com.fxz.auth.handler.CheckSumHandler;
import com.fxz.auth.handler.HeartBeatHandler;
import com.fxz.fts.codec.Message2BytesCodec;
import com.fxz.fts.config.Const;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.ErrorMsg;
import com.fxz.fts.messagefactory.MessageFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: FtsServer
 * @Description: 命令工厂与底层通讯中间件的交汇处
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:44:05
 */

public class FtsServer extends Thread {
	int port;
	Logger logger = Logger.getLogger(FtsServer.class);
	FtsConfig ftsConfig;
	AuthConfig authConfig;

	public FtsServer(int port) {
		this.port = port;
		ftsConfig = new FtsConfig();
		ftsConfig.setPort(port);
		authConfig = new AuthConfig();
	}

	public FtsServer(String ip, int port) {
		ftsConfig = new FtsConfig();
		ftsConfig.setIp(ip);
		ftsConfig.setPort(port);
		authConfig = new AuthConfig();
	}

	public FtsServer(FtsConfig ftsConfig) {
		this.ftsConfig = ftsConfig;
		authConfig = new AuthConfig();
	}

	public FtsServer(FtsConfig ftsConfig, AuthConfig authConfig) {
		this.ftsConfig = ftsConfig;
		this.authConfig = authConfig;
	}

	public static void main(String[] args) {

		new FtsServer(9000).start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_TIMEOUT, 1000).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new Message2BytesCodec());
					authConfig.setServer(true);
					Const.ftsConfig = ftsConfig;
					if (!authConfig.getMessageDigest().equalsIgnoreCase("none")) {
						ch.pipeline().addLast(new CheckSumHandler(authConfig.getMessageDigest()));
					}
					ch.pipeline().addLast(new AuthHandler(authConfig));
					ch.pipeline().addLast(new IdleStateHandler(60 , 0, 0, TimeUnit.SECONDS));
					ch.pipeline().addLast(new HeartBeatHandler());
					ch.pipeline().addLast(new ChannelHandlerAdapter() {
						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							System.out.println("channelActive");
							super.channelActive(ctx);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							System.out.println("channelInactive");
							super.channelInactive(ctx);
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							// TODO Auto-generated method stub
							if (msg instanceof BaseMessage) {
								/*
								 * 使用命令工厂处理收到的报文
								 */
								try {
									MessageFactory.getProcessor(((BaseMessage) msg).getType()).process(ctx, (BaseMessage) msg);
								} catch (IOException e) {
									ctx.writeAndFlush(new ErrorMsg("N/A", e.getMessage()));
									ctx.close();
									logger.error(e);
								}
							} else {
								logger.warn("message format error message type is->" + msg.getClass().getName());
							}
							ReferenceCountUtil.release(msg);
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
							// TODO Auto-generated method stub
							ctx.close();
							cause.printStackTrace();
						}

						@Override
						public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
							// TODO Auto-generated method stub
							System.out.println("disconnect");
						}

					});
				}

			}).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
			System.out.println("Listen Starting");
			ChannelFuture f = b.bind(ftsConfig.getPort()).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
}
