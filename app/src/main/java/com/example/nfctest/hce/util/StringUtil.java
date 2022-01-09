package com.example.nfctest.hce.util;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
    private static String TAG = "STRING_UTIL";
    public static boolean isBlank(String string) {
        return string == null || string.isEmpty();
    }
    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }
    public static String getPaddedString(int value,int size) {
        Log.d(TAG,"Get Padded String from int:"+value+" size:"+size);
        return StringUtils.leftPad(String.valueOf(value),size,"0");
    }

    public static String getPaddedString(float value,int size) {
        Log.d(TAG,"Get Padded String from float:"+value+" size:"+size);

        float val = Math.round(value*100);
        String paddedVal =  StringUtils.leftPad(String.valueOf((int)val),size,"0");
        Log.d(TAG,"Padded String:"+paddedVal);
        return paddedVal;
    }
}
