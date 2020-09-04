package com.yoga.utility.uploader.model;

import com.yoga.core.utils.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "system_files")
public class UploadFile {

	@Id
	@KeySql(useGeneratedKeys = true)
	private Long id;
	@Column(name = "tenant_id")
	private Long tenantId;

	@Column(name = "filename")
	private String filename;
	@Column(name = "local_path")
	private String localPath;
	@Column(name = "remote_url")
	private String remoteUrl;

	@Column(name = "purpose")
	private String purpose;
	@Column(name = "content_type")
	private String contentType;
	@Column(name = "file_size")
	private Long fileSize;

	@Column(name = "create_time")
	private Date createTime;

	public UploadFile(long tenantId, String filename, String localPath, String remoteUrl, String purpose, String contentType, long fileSize) {
		this.tenantId = tenantId;
		if (StringUtil.isBlank(filename)) this.filename = getFilename(localPath);
		else this.filename = filename;
		this.localPath = localPath;
		this.remoteUrl = remoteUrl;
		this.purpose = purpose;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.createTime = new Date();
	}
	private String getFilename(String path) {
		File file = new File(path);
		return file.getName();
	}
}
