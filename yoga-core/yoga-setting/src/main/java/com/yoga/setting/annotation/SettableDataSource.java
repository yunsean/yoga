package com.yoga.setting.annotation;

import java.util.List;

public interface SettableDataSource {
    interface SettableDataItem {
        String getId();
        String getName();
    }
    List<SettableDataItem> allItems(long tenantId, String module, String key);
}
