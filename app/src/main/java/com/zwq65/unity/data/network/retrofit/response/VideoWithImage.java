package com.zwq65.unity.data.network.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zwq65 on 2017/08/15
 * 休息视频data只有url，没有缩略图，所以只能和美图组合起来以美观一下
 */

public class VideoWithImage implements Parcelable {
    private RestVideoResponse.Video video;
    private Image image;


    public VideoWithImage(RestVideoResponse.Video video, Image image) {
        this.video = video;
        this.image = image;
    }

    protected VideoWithImage(Parcel in) {
        video = in.readParcelable(RestVideoResponse.Video.class.getClassLoader());
        image = in.readParcelable(Image.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(video, flags);
        dest.writeParcelable(image, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoWithImage> CREATOR = new Creator<VideoWithImage>() {
        @Override
        public VideoWithImage createFromParcel(Parcel in) {
            return new VideoWithImage(in);
        }

        @Override
        public VideoWithImage[] newArray(int size) {
            return new VideoWithImage[size];
        }
    };

    public RestVideoResponse.Video getVideo() {
        return video;
    }

    public void setVideo(RestVideoResponse.Video video) {
        this.video = video;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
