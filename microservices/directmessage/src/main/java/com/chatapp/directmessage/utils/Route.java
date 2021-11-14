package com.chatapp.directmessage.utils;

public class Route {

    private Route() {

    }

    private static final String ID = "id/";

    public static class User {
        private static final String HOST = "http://USER-SERVICE/";
        private static final String PATH = HOST + "api/v1/user/";

        private User() {
        }

        public static class GET {
            private GET() {
            }
            public static final String BY_ID = PATH + ID;
            public static final String BY_USERNAME = PATH;
        }

    }

}
