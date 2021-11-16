package com.chatapp.websocket.utils;

public class Route {
    private Route() {
    }

    public static class WS {
        private WS() {
        }

        private static final String PATH = "/channel/";
        public static class SEND {
            private SEND() {
            }
            public static final String DM = PATH;
        }
    }
}
