package com.yoga.utility.uploader.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yoga.utility.uploader.model.UploadFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadFileVo {

	private Long id;
	private Long size;
	private String url;
	private String name;
	private String contentType;

	public UploadFileVo(UploadFile file) {
		this.id = file.getId();
		this.size = file.getFileSize();
		this.url = file.getRemoteUrl();
		this.name = file.getFilename();
		this.contentType = file.getContentType();
	}
}
