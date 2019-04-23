package com.demon.activitychange.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LIXH on 2019/4/23.
 * email lixhVip9@163.com
 * des
 */
public class AccessibilityEventBean implements Serializable {
    CharSequence packageName;
    CharSequence name;
    List<CharSequence> text;

    public CharSequence getPackageName() {
        return this.packageName;
    }

    public void setPackageName(CharSequence packageName) {
        this.packageName = packageName;
    }

    public CharSequence getName() {
        return this.name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public List<CharSequence> getText() {
        return this.text;
    }

    public void setText(List<CharSequence> text) {
        this.text = text;
    }
}
