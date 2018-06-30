package com.yoga.tenant.uploader.service;

import com.yoga.core.exception.BusinessException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.DateUtil;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.tenant.uploader.model.UploadFile;
import com.yoga.tenant.uploader.repo.UploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service("globalFileUploadService")
public class UploadService extends BaseService {

    @Autowired
    private ImageConvertService imageConvertService;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UploadRepository uploadRepository;

    public String resize(String original, int width, int height) {
        if (width != 0 && height != 0) {
            String path = imageConvertService.drawImg(original, width, height);
            if (path != null && (new File(path)).exists()) {
                new File(original).delete();
                return path;
            }
        }
        return original;
    }

    public File generateFilePath(long tenantId, String name, Integer addDate) {
        int index = 0;
        String now = DateUtil.formatDate(new Date(), "yyyyMMddHHmmssSSS");
        String parentPath = propertiesService.getFileLocalPath(tenantId);
        new File(parentPath).mkdirs();
        if ( null != addDate && addDate == 0){
            File destFile = new File(parentPath, name);
            return destFile;
        }else {
            while (true) {
                int dot = name.lastIndexOf('.');
                String base = (dot == -1) ? name : name.substring(0, dot);
                String extension = (dot == -1) ? "" : name.substring(dot);
                String newName = base;
                newName = base+ "_" + now;
                if (index > 0) newName += "_" + index;
                newName += extension;
                File destFile = new File(parentPath, newName);
                if (!destFile.exists()) return destFile;
                else index++;
            }
        }
    }

    public UploadFile add(long tenantId, String file, String purpose) {
        long size = new File(file).length();
        String url = file.replace(propertiesService.getFileLocalPath(), propertiesService.getFileRemotePath());
        url = url.replace("\\", "/");
        UploadFile uploadFile = new UploadFile(tenantId, new Date(), file, url, purpose, size);
        uploadFile.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_G_FILES_ID));
        uploadRepository.save(uploadFile);
        return uploadFile;
    }

    public void delete(long tenantId, long id) {
        UploadFile file = uploadRepository.findOne(id);
        if (file == null || file.getTenantId() != tenantId) throw new BusinessException("文件不存在！");
        new File(file.getLocalPath()).delete();
        uploadRepository.delete(file);
    }
}
