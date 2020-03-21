package com.fxz.fts.Test;

import java.io.IOException;
import com.fxz.auth.config.AuthConfig;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IDataProcessor;
import com.fxz.fts.server.FtsServer;
import com.fxz.fts.task.FtsTask;
import io.netty.channel.ChannelHandlerContext;

public class ServerMain {

	public static void main(String[] args) {
		FtsConfig ftsConfig = new FtsConfig();
		ftsConfig.setPort(9000);
		System.out.println();
		System.out.println();
		ftsConfig.setDataProcessor(new IDataProcessor() {

			@Override
			public byte[] outstream(byte[] buffer) throws IOException {
				// TODO Auto-generated method stub

				return buffer;
			}

			@Override
			public void onTaskSucc(FtsTask ftsTask) {
				// TODO Auto-generated method stub
				System.out.println("Task Succed->" + ftsTask);
			}

			@Override
			public void onTaskFail(FtsTask ftsTask) {
				// TODO Auto-generated method stub
				System.out.println("Task Failed ->" + ftsTask);
			}

			@Override
			public boolean onStart(FtsConfig ftsConfig, FtsTask ftsTask) {
				// TODO Auto-generated method stub
				System.out.println("Accept task->" + ftsTask);
				return true;
			}

			@Override
			public void onMessage(ChannelHandlerContext ctx, StringMsg stringMsg) {
				// TODO Auto-generated method stub
				System.out.println("get Message->" + stringMsg);
			}

			@Override
			public byte[] instream(byte[] buffer) throws IOException {
				// TODO Auto-generated method stub

				return buffer;
			}

			@Override
			public void init(Object obj) {
				// TODO Auto-generated method stub

			}
		});
		AuthConfig authConfig = new AuthConfig();
		authConfig.setServer(true);
		authConfig.setSymEncrypt("none");
		authConfig.setMessageDigest("none");
		new FtsServer(ftsConfig, authConfig).start();
		System.out.println("Fts Server Started!");
	}
}
