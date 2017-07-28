package com.zwq65.unity.data.network.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zwq65 on 2017/07/20
 */

public class WelfareResponse {

    /**
     * error : false
     * results : [{"_id":"596ea620421aa90c9203d3bc","createdAt":"2017-07-19T08:21:52.67Z","desc":"7-19","publishedAt":"2017-07-19T13:23:20.375Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg","used":true,"who":"代码家"},{"_id":"596d5739421aa97de5c7c959","createdAt":"2017-07-18T08:32:57.632Z","desc":"7-18","publishedAt":"2017-07-18T16:12:55.381Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhnqjm1vczj20rs0rswia.jpg","used":true,"who":"daimajia"},{"_id":"59681987421aa97de5c7c92f","createdAt":"2017-07-14T09:08:23.898Z","desc":"7 月 17 日","publishedAt":"2017-07-17T12:22:21.307Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhj5228gwdj20u00u0qv5.jpg","used":true,"who":"带马甲"},{"_id":"596819b7421aa90ca209c45f","createdAt":"2017-07-14T09:09:11.591Z","desc":"RIP","publishedAt":"2017-07-14T13:24:31.177Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhj53yz5aoj21hc0xcn41.jpg","used":true,"who":"代码家"},{"_id":"5966c4b8421aa90ca209c452","createdAt":"2017-07-13T08:54:16.862Z","desc":"7-13","publishedAt":"2017-07-13T12:28:15.167Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhhz28n9vyj20u00u00w9.jpg","used":true,"who":"daimajia"},{"_id":"59656ba8421aa97de5c7c91b","createdAt":"2017-07-12T08:22:00.873Z","desc":"7-12","publishedAt":"2017-07-12T13:05:59.766Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhgsi7mqa9j20ku0kuh1r.jpg","used":true,"who":"daimajia"},{"_id":"59641954421aa90c9203d362","createdAt":"2017-07-11T08:18:28.124Z","desc":"佐佐木希","publishedAt":"2017-07-11T13:46:33.911Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhfmsbxvllj20u00u0q80.jpg","used":true,"who":"代码家"},{"_id":"5962c411421aa90ca209c425","createdAt":"2017-07-10T08:02:25.353Z","desc":"7-10","publishedAt":"2017-07-10T12:48:49.297Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg","used":true,"who":"daimajia"},{"_id":"595ed766421aa90ca209c407","createdAt":"2017-07-07T08:35:50.172Z","desc":"7-7","publishedAt":"2017-07-07T12:14:57.685Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhb0t7ob2mj20u011itd9.jpg","used":true,"who":"daimajia"},{"_id":"595d82f6421aa90ca3bb6aaa","createdAt":"2017-07-06T08:23:18.945Z","desc":"07-06","publishedAt":"2017-07-06T11:57:03.770Z","source":"chrome","type":"福利","url":"https://ws1.sinaimg.cn/large/610dc034gy1fh9utulf4kj20u011itbo.jpg","used":true,"who":"代码家"}]
     */

    private boolean error;
    private List<Image> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Image> getResults() {
        return results;
    }

    public void setResults(List<Image> results) {
        this.results = results;
    }

    public static class Image implements Parcelable {
        /**
         * _id : 596ea620421aa90c9203d3bc
         * createdAt : 2017-07-19T08:21:52.67Z
         * desc : 7-19
         * publishedAt : 2017-07-19T13:23:20.375Z
         * source : chrome
         * type : 福利
         * url : https://ws1.sinaimg.cn/large/610dc034ly1fhovjwwphfj20u00u04qp.jpg
         * used : true
         * who : 代码家
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        protected Image(Parcel in) {
            _id = in.readString();
            createdAt = in.readString();
            desc = in.readString();
            publishedAt = in.readString();
            source = in.readString();
            type = in.readString();
            url = in.readString();
            used = in.readByte() != 0;
            who = in.readString();
        }

        public static final Creator<Image> CREATOR = new Creator<Image>() {
            @Override
            public Image createFromParcel(Parcel in) {
                return new Image(in);
            }

            @Override
            public Image[] newArray(int size) {
                return new Image[size];
            }
        };

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(_id);
            dest.writeString(createdAt);
            dest.writeString(desc);
            dest.writeString(publishedAt);
            dest.writeString(source);
            dest.writeString(type);
            dest.writeString(url);
            dest.writeByte((byte) (used ? 1 : 0));
            dest.writeString(who);
        }
    }
}
