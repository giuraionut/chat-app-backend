package com.chatapp.message.utils;

public class Route {

    private Route() {

    }

    public static class User {
        private static final String HOST = "http://USER-SERVICE/";
        private static final String PATH = HOST + "api/v1/user/";

        private User() {
        }

        public static class GET {
            private GET() {
            }

            public static final String BY_ID = PATH;
        }

    }

    public static class WS {
        private WS() {
        }

        private static final String PATH = "/private/";

        public static class SEND {
            private SEND() {
            }

            public static final String DM = PATH;
        }
    }

}
