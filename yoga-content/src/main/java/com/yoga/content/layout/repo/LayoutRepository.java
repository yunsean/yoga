package com.yoga.content.layout.repo;

import com.yoga.content.layout.model.Layout;
import com.yoga.content.enums.LayoutType;
import com.yoga.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LayoutRepository extends BaseRepository<Layout, Long> {

    List<Layout> findByTenantIdAndTemplateIdAndType(long tenantId, long templateId, LayoutType type);
    List<Layout> findByTenantIdAndTemplateId(long tenantId, long templateId);
}
