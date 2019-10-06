package com.comp90018.H1Calendar.utils;

import com.comp90018.H1Calendar.R;

/**
 * Created by acer-pc on 2016/4/28.
 */
public class ColorUtils {

    public static int getColorFromStr(String s){
        int colorId = 0;
        switch (s) {
            case "Default":
                colorId = R.color.colorPrimaryDark;
                break;
            case "Green":
                colorId = R.color.Green;
                break;
            case "Yellow":
                colorId = R.color.Yellow;
                break;
            case "Red":
                colorId = R.color.Red;
                break;
            case "Blue":
                colorId = R.color.Blue;
                break;
        }
        return colorId;
    }
}
