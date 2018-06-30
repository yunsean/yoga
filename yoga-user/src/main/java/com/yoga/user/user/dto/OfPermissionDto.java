package com.yoga.user.user.dto;

import com.yoga.core.data.BaseEnum;
import com.yoga.user.basic.TenantDto;


public class OfPermissionDto extends TenantDto {

    public enum Op implements BaseEnum<String> {
        OR("OR"),
        AND("AND");
        String code;
        Op(String code) {
            this.code = code;
        }
        @Override
        public String getCode() {
            return code;
        }
        @Override
        public String getName() {
            return toString();
        }
    }

    private String[] permissions;
    private Op op;
    private int pageIndex;
    private int pageSize = 10;

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public Op getOp() {
        return op;
    }

    public void setOp(Op op) {
        this.op = op;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
