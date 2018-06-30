package com.yoga.tenant.tenant.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.tenant.tenant.model.TemplateSetting;

import java.util.List;

public interface TemplateSettingRepository extends BaseRepository<TemplateSetting, Long> {
    TemplateSetting findOneByTemplateIdAndModuleAndKey(long templateId, String module, String key);
    List<TemplateSetting> findByTemplateId(long templateId);
}
