package com.yoga.ewedding.counselor.dto;

import com.yoga.user.basic.TenantDto;
import org.hibernate.validator.constraints.NotEmpty;

public class ProveDto extends TenantDto {

    @NotEmpty(message = "请输入顾问身份信息")
    private String token;
    private String pid;
    private String pidFront;
    private String pidBack;
    private String[] images;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getPid() {
        return pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPidFront() {
        return pidFront;
    }
    public void setPidFront(String pidFront) {
        this.pidFront = pidFront;
    }

    public String getPidBack() {
        return pidBack;
    }
    public void setPidBack(String pidBack) {
        this.pidBack = pidBack;
    }

    public String[] getImages() {
        return images;
    }
    public void setImages(String[] images) {
        this.images = images;
    }
}
