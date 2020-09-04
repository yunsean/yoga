package com.yoga.core.property;

import com.yoga.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PropertiesService {

    @Value("${app.system.base_url:http://localhost:8200}")
    private String sysBaseurl;
    public String getSysBaseurl() {
        return sysBaseurl;
    }

    @Value("${app.file.temp_path:/tmp}")
    private String fileTempPath;
    @Value("${app.file.local_path:/tmp}")
    private String fileLocalPath;
    @Value("${app.file.remote_path:http://localhost:88}")
    private String fileRemotePath;
    @Value("${app.file.append_date:true}")
    private boolean fileAppendDate;
    @Value("${app.file.append_tenantId:true}")
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
        if (fileAppendDate) localUrl += "/" + DateUtil.format(new Date(), "yyyy-MM-dd");
        localUrl += "/";
        return localUrl;
    }

    @Value("${app.nginx.pdfviewer_url:/pdf.js/web/viewer.html}")
    private String pdfViewerUrl;
    public String getPdfViewerUrl() {
        return pdfViewerUrl;
    }

    @Value("${app.admin.superuser.username:SUPERADMIN}")
    private String adminSuperUser;
    @Value("${app.admin.superuser.password:358c7940f3bc058dad999ba616e38f1e}")    //Surp@ss2@!6
    private String adminSuperPwd;
    @Value("${app.admin.patchca:true}")
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

    @Value("${app.system.tenant-alias:租户}")
    private String tenantNameAlias;
    public String getTenantAlias() {
        return tenantNameAlias;
    }
}
