package com.fxz.fts.config;

import com.fxz.fts.processor.IDataProcessor;
import com.fxz.fts.processor.IProcessor;
import com.fxz.fts.processor.impl.FtsDataProcessor;
import com.fxz.fts.processor.impl.FtsProcessor;

public class FtsConfig {
    private boolean ischecksum = false;
    private boolean auth = false;
    private String ip = "0.0.0.0";
    private int port;
    private int bothtimeout = 90;
    private IProcessor processor = new FtsProcessor();
    private IDataProcessor dataProcessor = new FtsDataProcessor();

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public IDataProcessor getDataProcessor() {
        return dataProcessor;
    }

    public void setDataProcessor(IDataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public boolean isIschecksum() {
        return ischecksum;
    }

    public void setIschecksum(boolean ischecksum) {
        this.ischecksum = ischecksum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getBothtimeout() {
        return bothtimeout;
    }

    public void setBothtimeout(int bothtimeout) {
        this.bothtimeout = bothtimeout;
    }

    public IProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(IProcessor processor) {
        this.processor = processor;
    }

}
