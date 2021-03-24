package com.yoga.setting.model;

import com.yoga.core.utils.StringUtil;
import com.yoga.setting.annotation.Settable;

import java.util.HashSet;
import java.util.Set;

public class SettableItem{
    private String module;
    private String key;
    private String name;
    private Set<String> showWith = new HashSet<>();
    private String url;
    private String value;
    private String showValue;
    private boolean systemOnly;
    private boolean showPage;
    private Class type;
    private String defValue;
    private String placeHolder;

    public SettableItem(Settable settable, String url) {
        this.module = settable.module();
        this.key = settable.key();
        this.name = settable.name();
        String[] withs = settable.showWith();
        for (String with : withs) if (StringUtil.isNotBlank(with)) showWith.add(with);
        this.url = url;
        this.systemOnly = settable.systemOnly();
        this.showPage = settable.showPage();
        this.type = settable.type();
        this.defValue = settable.defaultValue();
        this.placeHolder = settable.placeHolder();
        if (this.module == null) this.module = "";
        if (this.key == null) this.key = "";
        if (this.name == null) this.name = "";
    }
    public SettableItem(SettableItem src) {
        this.module = src.module;
        this.key = src.key;
        this.name = src.name;
        this.showWith = src.showWith;
        this.url = src.url;
        this.value = src.value;
        this.showValue = src.showValue;
        this.systemOnly = src.systemOnly;
        this.showPage = src.showPage;
        this.defValue = src.defValue;
        this.placeHolder = src.placeHolder;
        this.type = src.type;
    }
    public String getModule() {
        return module;
    }
    public String getKey() {
        return key;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public String getValue() {
        return value;
    }
    public String getShowValue() {
        return showValue;
    }
    public boolean isSystemOnly() {
        return systemOnly;
    }
    public boolean isShowPage() {
        return showPage;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setShowValue(String showValue) {
        this.showValue = showValue;
    }
    public Class getType() {
        return type;
    }
    public String getDefValue() {
        return defValue;
    }
    public String getPlaceHolder() {
        return placeHolder;
    }

    public boolean matchFilter(String filter) {
        return module.contains(filter) || key.contains(filter) || name.contains(filter);
    }
    public boolean matchModule(String[] modules) {
        if (modules == null) return true;
        boolean found = false;
        for (String module : modules) {
            if (this.module.equals(module)) {
                found = true;
                break;
            }
        }
        if (!found) return false;
        if (showWith.size() > 0) {
            for (String module : modules) {
                if (this.showWith.contains(module)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }
}
