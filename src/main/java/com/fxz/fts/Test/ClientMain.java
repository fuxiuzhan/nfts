package com.fxz.fts.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.fxz.auth.config.AuthConfig;
import com.fxz.fts.config.FtsConfig;
import com.fxz.fts.message.StringMsg;
import com.fxz.fts.processor.IDataProcessor;
import com.fxz.fts.server.FtsClient;
import com.fxz.fts.task.FtsFile;
import com.fxz.fts.task.FtsTask;
import com.fxz.fts.utils.FileUtils;

import io.netty.channel.ChannelHandlerContext;

public class ClientMain {

    public static void main(String[] args) {

        Queue<FtsTask> taskQ = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            FtsFile ftsFile = new FtsFile();
            ftsFile.setFileName("dns_sys.sql");
            ftsFile.setFileNameto("2344.txt");
            ftsFile.setFilePathto("C:\\Temp");
            ftsFile.setFilePath("d:\\");
            ftsFile.setOverWrite(true);
            // Queue<FtsTask> taskQ = new LinkedList<>();
            ftsFile.setFileNameto(i + ".txt");
            FtsTask ftsTask = geTask(ftsFile);
            if (ftsTask == null) {
                System.out.println("FileNotFound Skipped this File->" + ftsFile);
                continue;
            }
            taskQ.add(ftsTask);
        }
        FtsConfig ftsConfig = new FtsConfig();
        ftsConfig.setDataProcessor(new IDataProcessor() {
            @Override
            public byte[] outstream(byte[] buffer) throws IOException {
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

                return buffer;
            }

            @Override
            public void init(Object obj) {
                // TODO Auto-generated method stub

            }
        });
        ftsConfig.setIp("127.0.0.1");
        ftsConfig.setPort(9000);
        AuthConfig authConfig = new AuthConfig();
        authConfig.setSymEncrypt("none");
        authConfig.setMessageDigest("none");
        new FtsClient(ftsConfig, authConfig, taskQ).start();
        System.out.println("Client Started!");
    }

    public static FtsTask geTask(FtsFile ftsFile) {
        FtsTask ftsTask = new FtsTask();
        ftsTask.setFtsFile(ftsFile);
        ftsTask.setHashType("md5");
        try {
            ftsTask.setHashCode(FileUtils.getFileHash(ftsTask.getHashType(), ftsFile.getFilePath() + "\\" + ftsFile.getFileName(), 0L));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e.getLocalizedMessage());
            return null;
        }
        ftsFile.setFileSize(new File(ftsFile.getFilePath() + "\\" + ftsFile.getFileName()).length());
        return ftsTask;
    }
}
