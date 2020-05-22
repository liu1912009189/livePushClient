package com.example.livepushclient.util;

import com.laifeng.sopcastsdk.entity.Size;

/**
 * Created by liule on 2020/5/22.
 * ----2020/5/22-------liule----xxxxxxxxx--
 */
public class StringUtil {
    public static Size parseSize(String resolution){
        String[] split = resolution.split(",");
        Size size = new Size(360,640);
        if(split != null &&split.length>0){
            size.width = Integer.parseInt(split[0]);
            size.hight = Integer.parseInt(split[1]);
        }else {
            size.width = 360;
            size.hight = 640;
        }
        return size;
    }
}
