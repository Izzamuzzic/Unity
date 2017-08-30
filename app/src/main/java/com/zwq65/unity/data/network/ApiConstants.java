package com.zwq65.unity.data.network;

/**
 * Created by zwq65 on 2017/07/20
 */

public class ApiConstants {
    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     * <p>
     * 数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     * 请求个数： 数字，大于0
     * 第几页：数字，大于0
     * e.g. http://gank.io/api/data/福利/10/1
     */
    public static String GANK_IO_HOST = "http://gank.io/api/data/";
    public static final String Welfare = "福利/";
    public static final String RestVideo = "休息视频/";
    public static final String Android = "Android/";
    public static final String IOS = "iOS/";
    public static final String Qianduan = "前端/";

    public static final String defaultPageSize = "10";
    public static final String morePageSize20 = "20";
}
