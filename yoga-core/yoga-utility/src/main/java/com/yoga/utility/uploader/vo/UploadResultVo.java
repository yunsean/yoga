package com.yoga.utility.uploader.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadResultVo {
    private String result;
    private String url;
    private String file;
    private String message;
    private String name;
    private long size;
    private long id;

    public UploadResultVo() {
        this.result = "ok";
    }
    public UploadResultVo(String url, String file, long fileId, String name, long size) {
        this.result = "ok";
        this.url = url;
        this.file = file;
        this.id = fileId;
        this.name = name;
        this.size = size;
    }
    public UploadResultVo(String message) {
        this.result = "failed";
        this.message = message;
    }
}
