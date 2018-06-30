package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BinaryUploader {
	public static final State save(HttpServletRequest request, Map<String, Object> conf) {
		if (request instanceof MultipartHttpServletRequest) {
			try {
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
				if (multipartResolver.isMultipart(request)) {
					MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
					Iterator<String> iter = multiRequest.getFileNames();
					MultipartFile file = null;
					while (iter.hasNext()) {
						file = multiRequest.getFile(iter.next());
						if (file != null) {
							break;
						}
						file = null;
					}
					if (file == null) {
						return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
					}
					String savePath = (String) conf.get("savePath");
					String originFileName = file.getOriginalFilename();
					String suffix = FileType.getSuffixByFilename(originFileName);
					originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
					savePath = savePath + suffix;
					long maxSize = ((Long) conf.get("maxSize")).longValue();
					if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
						return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
					}
					savePath = PathFormat.parse(savePath, originFileName);
					String physicalPath = (String) conf.get("rootPath") + savePath;
					File output = new File(physicalPath);
					int prefix = 0;
					while (output.exists()) {
						savePath = PathFormat.parse(savePath, String.valueOf(prefix++) + "_" + originFileName);
						physicalPath = (String) conf.get("rootPath") + savePath;
						output = new File(physicalPath);
					}
					InputStream is = file.getInputStream();
					State storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
					is.close();
					if (storageState.isSuccess()) {
						storageState.putInfo("url", PathFormat.format(savePath));
						storageState.putInfo("type", suffix);
						storageState.putInfo("original", originFileName + suffix);
					}
					return storageState;
				}
			} catch (Exception e) {
				return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
			}
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
