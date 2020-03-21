/**
 * @Copyright © 2018 fuxiuzhn Fts Team All rights reserved.
 * @Package: com.fxz.fts.server 
 * @author: fuxiuzhan@163.com   
 * @date: 2018年8月30日 下午5:44:16 
 * 
 */
package com.fxz.fts.server;

import java.util.Queue;
import java.util.UUID;
import org.apache.log4j.Logger;
import com.fxz.auth.config.AuthConfig;
import com.fxz.auth.handler.AuthHandler;
import com.fxz.auth.handler.CheckSumHandler;
import com.fxz.fts.codec.Message2BytesCodec;
import com.fxz.fts.config.Const;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.AborTransferMsg;
import com.fxz.fts.message.BaseMessage;
import com.fxz.fts.message.CompleteMsg;
import com.fxz.fts.message.ErrorMsg;
import com.fxz.fts.message.FileBlockMsg;
import com.fxz.fts.message.FileRequestReplyMsg;
import com.fxz.fts.message.RequestBlcockMsg;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IProcessor;
import com.fxz.fts.task.FtsTask;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

/**
 * @ClassName: FtsClient
 * @Description: 实现连接服务端，文件传输等操作，具备队列参数列表，可以在一个chanal中传输多个文件的能力
 * @author: fuxiuzhan@163.com
 * @date: 2018年8月30日 下午5:44:16
 */

public class FtsClient extends Thread {
	String file;
	IProcessor ftsProcessor;
	Logger logger = Logger.getLogger(FtsClient.class);
	FtsConfig ftsConfig;
	Queue<FtsTask> taskQ;
	AuthConfig authConfig;

	public FtsClient(FtsConfig ftsConfig, Queue<FtsTask> taskQ) {
		this.ftsConfig = ftsConfig;
		this.taskQ = taskQ;
		authConfig = new AuthConfig();
	}

	public FtsClient(FtsConfig ftsConfig, AuthConfig authConfig) {
		this.authConfig = authConfig;
		this.ftsConfig = ftsConfig;
	}

	public FtsClient(FtsConfig ftsConfig, AuthConfig authConfig, Queue<FtsTask> taskQ) {
		this.authConfig = authConfig;
		this.ftsConfig = ftsConfig;
		this.taskQ = taskQ;
	}

	public FtsClient(Queue<FtsTask> taskQ) {
		ftsConfig = new FtsConfig();
		this.taskQ = taskQ;
		authConfig = new AuthConfig();
	}

	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_LINGER, 0).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new Message2BytesCodec());
					if (!authConfig.getMessageDigest().equalsIgnoreCase("none")) {
						ch.pipeline().addLast(new CheckSumHandler(authConfig.getMessageDigest()));
					}
					ch.pipeline().addLast(new AuthHandler(authConfig));
					ch.pipeline().addLast(new ChannelHandlerAdapter() {
						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							ftsProcessor = ftsConfig.getProcessor();
							ftsProcessor.transfer_init(ctx, taskQ, ftsConfig);
						}

						@Override
						public void channelInactive(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							super.channelInactive(ctx);
						}

						@Override
						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
							// TODO Auto-generated method stub
							if (msg instanceof BaseMessage) {
								switch (((BaseMessage) msg).getType()) {
								case Const.MSG_ABORTRANFER:
									ftsProcessor.transfer_abor(ctx, new AborTransferMsg((BaseMessage) msg));
									break;
								case Const.MSG_COMPELTE:
									ftsProcessor.transfer_complete(ctx, new CompleteMsg((BaseMessage) msg));
									break;
								case Const.MSG_FILEBLOCK:
									ftsProcessor.transfer_processing(ctx, new FileBlockMsg((BaseMessage) msg));
									break;
								case Const.MSG_FILEREQUESTREPLY:
									ftsProcessor.transfer_start(ctx, new FileRequestReplyMsg((BaseMessage) msg));
									break;
								case Const.MSG_REQUESTBLOCK:
									ftsProcessor.transfer_processing(ctx, new RequestBlcockMsg((BaseMessage) msg));
									break;
								case Const.MSG_SEND_FILEREQUEST:
									break;
								case Const.MSG_STRINGMG:
									ftsProcessor.transfer_mesgtext(ctx, new StringMsg((BaseMessage) msg));
									break;
								case Const.MSG_ERROR:
									ftsProcessor.transfer_exception(ctx, new Exception((new ErrorMsg((BaseMessage) msg)).getError()));
									break;
								case Const.AUTH_HEARTBEAT:
									logger.info("recv heart beat message->" + msg);
									ctx.writeAndFlush(msg);
									break;
								default:
									break;
								}
							} else {
								logger.warn("message format error message type->" + msg.getClass().getName());
							}
							ReferenceCountUtil.release(msg);
						}

						@Override
						public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
							// TODO Auto-generated method stub
							System.out.println("channelReadComplete");
							super.channelReadComplete(ctx);
						}

						@Override
						public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
							if (ctx.channel().isWritable()) {
								ftsProcessor.transfer_processing(ctx, new FileBlockMsg(UUID.randomUUID().toString(), -1, new byte[] { 0x00, 0x00 }));
							}
						}

						@Override
						public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
							ftsProcessor.transfer_exception(ctx, cause);
						}

					});
				}

			});

			final ChannelFuture f = b.connect(ftsConfig.getIp(), ftsConfig.getPort()).sync();
			System.out.println("MessageSend");
			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			System.out.println("exit..");
		}
	}
}
