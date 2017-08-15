package com.zwq65.unity.data.network.retrofit.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by zwq65 on 2017/08/15
 */

public class RestVideoResponse {
    /**
     * error : false
     * results : [{"_id":"59906c33421aa90f4919c7e1","createdAt":"2017-08-13T23:11:47.518Z","desc":"DC最快的闪电侠，在你看标题时就已跑到银河尽头","publishedAt":"2017-08-14T17:04:50.266Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av13207512/","used":true,"who":"LHF"},{"_id":"598d1abb421aa90c9203d498","createdAt":"2017-08-11T10:47:23.747Z","desc":"乔布斯遗失珍贵访谈首曝光 乔帮主预见未来（中英字幕）","publishedAt":"2017-08-11T14:05:53.749Z","source":"chrome","type":"休息视频","url":"https://v.qq.com/x/cover/obr3rfx7xdatznl/b0113x7xx0m.html","used":true,"who":"lxxself"},{"_id":"59833082421aa97de5c7ca1d","createdAt":"2017-08-03T22:17:38.40Z","desc":"走进大众工厂，看一张铁皮是怎么变成汽车的（看条）","publishedAt":"2017-08-09T13:49:27.260Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12897095/","used":true,"who":"LHF"},{"_id":"59833107421aa90c9203d46b","createdAt":"2017-08-03T22:19:51.587Z","desc":"大学生历时一年完成的航拍中国","publishedAt":"2017-08-08T11:34:20.37Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12902834/","used":true,"who":"LHF"},{"_id":"59809b0d421aa90ca3bb6bc9","createdAt":"2017-08-01T23:15:25.145Z","desc":"【66熟肉】新一代劳斯莱斯幻影的十大亮点，你不知道就没法装逼了","publishedAt":"2017-08-03T12:08:07.258Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12824384/","used":true,"who":"LHF"},{"_id":"5980999f421aa97de5c7ca01","createdAt":"2017-08-01T23:09:19.786Z","desc":"【谷阿莫】5分鐘看完你跟猩猩一樣聰明欸的電影《猩球崛起1+2》","publishedAt":"2017-08-02T12:21:45.220Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12799493/","used":true,"who":"LHF"},{"_id":"597deb9d421aa90ca3bb6ba7","createdAt":"2017-07-30T22:22:21.321Z","desc":"【将军】几分钟看《西部世界》所有机器人都认为自己是人类，却被真人当作发泄工具","publishedAt":"2017-08-01T11:48:20.466Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12753992/","used":true,"who":"LHF"},{"_id":"5978ad1d421aa90ca3bb6b7a","createdAt":"2017-07-26T22:54:21.793Z","desc":"电影最TOP 65: 热血！盘点最经典的二战电影","publishedAt":"2017-07-27T14:16:33.773Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12595919/","used":true,"who":"LHF"},{"_id":"59760e4f421aa90ca209c4d3","createdAt":"2017-07-24T23:12:15.465Z","desc":"亚裔退伍军人参加《美国忍者勇士》，这身体素质全程跪着看完！","publishedAt":"2017-07-26T16:57:39.343Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12532105/","used":true,"who":"LHF"},{"_id":"5973521e421aa90ca3bb6b56","createdAt":"2017-07-22T21:24:46.388Z","desc":"【牛叔】几分钟看科幻片《终结者4》谁才是这个世界的救世主","publishedAt":"2017-07-25T15:25:42.391Z","source":"chrome","type":"休息视频","url":"http://www.bilibili.com/video/av12465641/","used":true,"who":"LHF"}]
     */
    private boolean error;
    private List<Video> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    public static class Video implements Parcelable{
        /**
         * _id : 59906c33421aa90f4919c7e1
         * createdAt : 2017-08-13T23:11:47.518Z
         * desc : DC最快的闪电侠，在你看标题时就已跑到银河尽头
         * publishedAt : 2017-08-14T17:04:50.266Z
         * source : chrome
         * type : 休息视频
         * url : http://www.bilibili.com/video/av13207512/
         * used : true
         * who : LHF
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

        protected Video(Parcel in) {
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

        public static final Creator<Video> CREATOR = new Creator<Video>() {
            @Override
            public Video createFromParcel(Parcel in) {
                return new Video(in);
            }

            @Override
            public Video[] newArray(int size) {
                return new Video[size];
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
