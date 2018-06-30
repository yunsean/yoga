package com.yoga.tenant.uploader.repo;

import com.yoga.tenant.uploader.model.UploadFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component("globalUploadFileRepository")
public interface UploadRepository extends CrudRepository<UploadFile, Long> {
}
