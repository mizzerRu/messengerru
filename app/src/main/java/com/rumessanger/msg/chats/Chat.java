package com.rumessanger.msg.chats;

public class Chat {

    private String chat_id, chat_name, usersId1, usersId2;

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getUsersId1() {
        return usersId1;
    }

    public void setUsersId1(String usersId1) {
        this.usersId1 = usersId1;
    }

    public String getUsersId2() {
        return usersId2;
    }

    public void setUsersId2(String usersId2) {
        this.usersId2 = usersId2;
    }

    public Chat(String chat_id, String chat_name, String usersId1, String usersId2) {
        this.chat_id = chat_id;
        this.chat_name = chat_name;
        this.usersId1 = usersId1;
        this.usersId2 = usersId2;
    }
}
