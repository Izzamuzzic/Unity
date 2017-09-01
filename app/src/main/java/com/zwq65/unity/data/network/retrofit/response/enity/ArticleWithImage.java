package com.zwq65.unity.data.network.retrofit.response.enity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zwq65 on 2017/09/01
 */

public class ArticleWithImage implements Parcelable {
    private Article article;
    private Image image;

    public ArticleWithImage(Article article, Image image) {
        this.article = article;
        this.image = image;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.article, flags);
        dest.writeParcelable(this.image, flags);
    }

    protected ArticleWithImage(Parcel in) {
        this.article = in.readParcelable(Article.class.getClassLoader());
        this.image = in.readParcelable(Image.class.getClassLoader());
    }

    public static final Parcelable.Creator<ArticleWithImage> CREATOR = new Parcelable.Creator<ArticleWithImage>() {
        @Override
        public ArticleWithImage createFromParcel(Parcel source) {
            return new ArticleWithImage(source);
        }

        @Override
        public ArticleWithImage[] newArray(int size) {
            return new ArticleWithImage[size];
        }
    };
}
