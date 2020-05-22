package com.example.livepushclient.util;

import android.view.View;

import com.example.livepushclient.R;


/**
 * View click utils.
 *
 * @Author:LiuLiWei
 */
public class ViewClickUtils {

  /**
   * Judge whether a clickable view is clicked quickly.
   * @param v
   * @return
   */
  public static boolean isFastClick(View v) {
    return isSpecificTimeClick(v, 750);
  }

  /**
   * Judge whether a clickable view is clicked within specific time..
   */
  public static boolean isSpecificTimeClick(View v, long interval) {
    try {
      long time = System.currentTimeMillis();
      long lastClickTime = 0;
      final Object tag = v.getTag(R.id.tag_click);
      if(null != tag && tag instanceof Long) {
        lastClickTime = (long) tag;
      }
      long timeD = time - lastClickTime;
      if (timeD > 0 && timeD < interval) {
        return true;
      }
      v.setTag(R.id.tag_click, time);
      return false;
    }catch (Exception e) {
      return false;
    }
  }

}
