package com.yoga.content.constants;

public class Contants {
    private static final String Content_CollectionPrefix = "contents";
    public static final String getCollectionName(long tenantId) {
        return Content_CollectionPrefix + ".t" + tenantId;
    }
}
