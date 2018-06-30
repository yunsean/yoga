package com.yoga.core.property;

import com.yoga.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesService {

    @Value("${sys.base_url:http://localhost:8200}")
    private String sysBaseurl;
    public String getSysBaseurl() {
        return sysBaseurl;
    }

    @Value("${file.temp_path:/tmp}")
    private String fileTempPath;
    @Value("${file.local_path:/tmp}")
    private String fileLocalPath;
    @Value("${file.remote_path:http://localhost:88}")
    private String fileRemotePath;
    @Value("${file.append_date:true}")
    private boolean fileAppendDate;
    @Value("${file.append_tenantId:true}")
    private boolean fileAppendTenantId;

    public String getFileTempPath() {
        return fileTempPath;
    }
    public String getFileLocalPath() {
        return fileLocalPath;
    }
    public String getFileRemotePath() {
        return fileRemotePath;
    }
    public boolean isFileAppendDate() {
        return fileAppendDate;
    }
    public boolean isFileAppendTenantId() {
        return fileAppendTenantId;
    }
    public String getFileLocalPath(long tenantId) {
        String localUrl = fileLocalPath;
        if (fileAppendTenantId) localUrl += "/tid" + tenantId;
        if (fileAppendDate) localUrl += "/" + DateUtil.formatDateShort(System.currentTimeMillis());
        localUrl += "/";
        return localUrl;
    }

    @Value("${fine.upload_url:/uploader/fine/upload}")
    private String fineUploadUrl;
    @Value("${fine.upload_url:/uploader/zui/upload}")
    private String zuiUploadUrl;
    @Value("${nginx.upload_path:/uploader/nginx/upload}")
    private String nginxUploadUrl;
    @Value("${nginx.pdfviewer_url:/pdf.js/admin/viewer.html}")
    private String pdfViewerUrl;
    public String getFineUploadUrl() {
        return fineUploadUrl;
    }
    public String getZuiUploadUrl() {
        return zuiUploadUrl;
    }
    public String getNginxUploadUrl() {
        return nginxUploadUrl;
    }
    public String getPdfViewerUrl() {
        return pdfViewerUrl;
    }

    @Value("${admin.super_user:SUPERADMIN}")
    private String adminSuperUser;
    @Value("${admin.super_pwd:358c7940f3bc058dad999ba616e38f1e}")    //Surp@ss2@!6
    private String adminSuperPwd;
    @Value("${admin.check_patchca:true}")
    private boolean adminCheckPatchca;

    public String getAdminSuperUser() {
        return adminSuperUser;
    }
    public String getAdminSuperPwd() {
        return adminSuperPwd;
    }
    public boolean isAdminCheckPatchca() {
        return adminCheckPatchca;
    }

    @Value("${cms.summary.length:100}")
    private int cmsSummaryLength = 100;
    public int getCmsSummaryLength() {
        return cmsSummaryLength;
    }
}
