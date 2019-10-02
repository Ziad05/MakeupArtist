package com.makeover.makeupartist.VideoGallery;

public class YoutubeVideoModel {

    private String videoId, title;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "YoutubeVideoModel{" + "videoId='" + videoId + '\'' + ", title='" + title + '\'' + '}';
    }
}
