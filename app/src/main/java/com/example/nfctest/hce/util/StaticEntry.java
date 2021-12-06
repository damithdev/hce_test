package com.example.nfctest.hce.util;

public class StaticEntry{
    public static class LogMessageEvent {
        public LogMessageEvent(String message){
            this.msg = message;
        }
        public String msg;
    }
}