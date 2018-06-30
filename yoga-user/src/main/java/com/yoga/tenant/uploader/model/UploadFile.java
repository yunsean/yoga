package com.yoga.tenant.uploader.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "g_files")
public class UploadFile {

	@Id
	private long id;
	@Column(name = "tenant_id")
	private long tenantId;

	@Column(name = "add_time")
	private Date addTime;
	@Column(name = "local_path")
	private String localPath;
	@Column(name = "remote_url")
	private String remoteUrl;

	@Column(name = "purpose")
	private String purpose;
	@Column(name = "file_size")
	private long fileSize;

	public UploadFile() {
	}
	public UploadFile(long tenantId, Date addTime, String localPath, String remoteUrl, String purpose, long fileSize) {
		this.tenantId = tenantId;
		this.addTime = addTime;
		this.localPath = localPath;
		this.remoteUrl = remoteUrl;
		this.purpose = purpose;
		this.fileSize = fileSize;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getTenantId() {
		return tenantId;
	}
	public void setTenantId(long tenantId) {
		this.tenantId = tenantId;
	}

	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}
	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
