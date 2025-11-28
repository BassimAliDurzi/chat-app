package se.sprinto.hakan.chatapp;

public class ChatServerMain {
    public static void main(String[] args) {
        new ChatServer(5560).start();
    }
}
