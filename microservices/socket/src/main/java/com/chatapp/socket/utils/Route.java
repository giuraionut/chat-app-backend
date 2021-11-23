package com.chatapp.socket.utils;

public class Route {

    private Route() {

    }
    public static class MESSAGE {
        private MESSAGE() {
        }

        private static final String HOST = "http://MESSAGE-SERVICE/";
        private static final String PATH = HOST + "api/v1/message/";

        public static final String CREATE = PATH;

    }

}
