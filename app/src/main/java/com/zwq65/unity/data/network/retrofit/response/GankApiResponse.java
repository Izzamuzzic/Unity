package com.zwq65.unity.data.network.retrofit.response;

/**
 * gank io返回数据基类
 */

public class GankApiResponse<T> extends ApiResponse {
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }


    public T results;


    public T getData() {
        return results;
    }

    public void setData(T data) {
        this.results = data;
    }


}
