package com.yoga.core.interfaces.wechat;

public class OutMessage {
    public OutMessageType type;
    public String mediaId;          //video voice image
    public String text;             //text
    public String thumbMediaId;     //music
    public String title;            //video music
    public String description;      //video music
    public String musicUrl;         //music
    public String hqMusicUrl;       //music

    public OutMessage(String text) {    //text
        this.type = OutMessageType.text;
        this.text = text;
    }
    public OutMessage(OutMessageType type, String mediaId) {    //image voice
        this.type = type;
        this.mediaId = mediaId;
    }
    public OutMessage(String mediaId, String title, String description) {   //video
        this.type = OutMessageType.video;
        this.mediaId = mediaId;
        this.title = title;
        this.description = description;
    }
    public OutMessage(String thumbMediaId, String title, String description, String musicUrl, String hqMusicUrl) {
        this.type = OutMessageType.music;
        this.thumbMediaId = thumbMediaId;
        this.title = title;
        this.description = description;
        this.musicUrl = musicUrl;
        this.hqMusicUrl = hqMusicUrl;
    }
}
