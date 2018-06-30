package com.yoga.tenant.uploader.model;

public class UploadResult {
    private String result;
    private String url;
    private String file;
    private String message;
    private long fileId;

    public UploadResult() {
        this.result = "ok";
    }
    public UploadResult(String url, String file, long fileId) {
        this.result = "ok";
        this.url = url;
        this.file = file;
        this.fileId = fileId;
    }
    public UploadResult(String message) {
        this.result = "failed";
        this.message = message;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public long getFileId() {
        return fileId;
    }
    public void setFileId(long fileId) {
        this.fileId = fileId;
    }
}
