package com.a2345.mimeplayer.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanzf on 2015/12/16.
 */
public class PatternUtil {
    public static String getValueForPattern(String content, String target){
        String value = null;
        if(null == content || null == target || content.length() < 1 || target.length() < 1)
            return null;
        Pattern pa = Pattern.compile(target);
        Matcher ma = pa.matcher(content);
        while (ma.find()){
            value = ma.group(1);
        }
        return value;
    }
}
