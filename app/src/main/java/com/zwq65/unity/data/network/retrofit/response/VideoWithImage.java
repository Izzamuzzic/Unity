package com.zwq65.unity.data.network.retrofit.response;

/**
 * Created by zwq65 on 2017/08/15
 * 休息视频data只有url，没有缩略图，所以只能和美图组合起来以美观一下
 */

public class VideoWithImage {
    private RestVideoResponse.Video video;
    private WelfareResponse.Image image;

    public VideoWithImage() {
    }

    public VideoWithImage(RestVideoResponse.Video video, WelfareResponse.Image image) {
        this.video = video;
        this.image = image;
    }

    public RestVideoResponse.Video getVideo() {
        return video;
    }

    public void setVideo(RestVideoResponse.Video video) {
        this.video = video;
    }

    public WelfareResponse.Image getImage() {
        return image;
    }

    public void setImage(WelfareResponse.Image image) {
        this.image = image;
    }
}
