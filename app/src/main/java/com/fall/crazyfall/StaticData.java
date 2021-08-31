package com.fall.crazyfall;

public enum StaticData {
    SHARED_PREF_NAME {
        @Override
        public String getData(){
            return "";
        }
    },
    URL {
        @Override
        public String getData(){
            return "";
        }
    },
    ONESIGNAL_APP_ID {
        @Override
        public String getData(){
            return "";
        }
    },
    AF_DEV_KEY{
        @Override
        public String getData(){
            return "";
        }
    },;

    public abstract String getData();
}