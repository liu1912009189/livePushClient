package com.example.livepushclient.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liule on 2020/5/22.
 * ----2020/5/22-------liule----xxxxxxxxx--
 */
public class LiveData implements Parcelable {
    public String minBps;
    public String maxBps;
    public String Fps;
    public String initBps;
    public String resolution;
    /**
     * 0开发 1测试
     */
    public String netFlag;

    public LiveData(){}

    protected LiveData(Parcel in) {
        minBps = in.readString();
        maxBps = in.readString();
        Fps = in.readString();
        initBps = in.readString();
        resolution = in.readString();
        netFlag = in.readString();
    }

    public static final Creator<LiveData> CREATOR = new Creator<LiveData>() {
        @Override
        public LiveData createFromParcel(Parcel in) {
            return new LiveData(in);
        }

        @Override
        public LiveData[] newArray(int size) {
            return new LiveData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(minBps);
        dest.writeString(maxBps);
        dest.writeString(Fps);
        dest.writeString(initBps);
        dest.writeString(resolution);
        dest.writeString(netFlag);
    }

    @Override
    public String toString() {
        return "LiveData{" +
                "minBps='" + minBps + '\'' +
                ", maxBps='" + maxBps + '\'' +
                ", Fps='" + Fps + '\'' +
                ", initBps='" + initBps + '\'' +
                ", resolution='" + resolution + '\'' +
                ", netFlag='" + netFlag + '\'' +
                '}';
    }
}
