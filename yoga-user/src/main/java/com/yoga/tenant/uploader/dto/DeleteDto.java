package com.yoga.tenant.uploader.dto;

import com.yoga.user.basic.TenantDto;

public class DeleteDto extends TenantDto {
	private long fileId;

	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
}
