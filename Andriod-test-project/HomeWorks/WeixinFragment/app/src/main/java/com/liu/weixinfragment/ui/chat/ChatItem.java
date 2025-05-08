package com.liu.weixinfragment.ui.chat;

public class ChatItem {
    private int avatarResId; // 头像资源ID
    private String name; // 昵称
    private String lastMessage; // 最后一条消息
    private String timestamp; // 时间戳

    public ChatItem(int avatarResId, String name, String lastMessage, String timestamp) {
        this.avatarResId = avatarResId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}