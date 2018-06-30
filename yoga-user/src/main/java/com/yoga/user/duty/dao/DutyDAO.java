package com.yoga.user.duty.dao;

import com.yoga.core.dao.BaseDAO;
import com.yoga.core.utils.SqlBuilder;
import com.yoga.core.utils.SqlBuilder.BuildResult;
import org.hibernate.bytecode.internal.javassist.BulkAccessor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;

@Repository
public class DutyDAO extends BaseDAO {

    //比插入级别低的，所有职务级别+1
    public void advanceDutyLevelAfter(int aboveOf, long tenantId) {
        String sql = "update s_duty set level = level + 1 where tenant_id = " + tenantId + " and level >= " + aboveOf;
        jdbcTemplate.execute(sql);
    }

    public void deleteDutyByLevel(int dutyLevel, long tenantId) {
        if (dutyLevel == 0) throw new RuntimeException("不允许删除[其他]职称！");
        String sql = "delete from s_duty where tenant_id = " + tenantId + " and level = " + dutyLevel;
        jdbcTemplate.execute(sql);
    }

    public void updateDutyName(long id, String name, String remark) {

        BuildResult buildResult = new SqlBuilder().put("update s_duty set name = ?", name, Types.VARCHAR)
                .put(", remark = ?", remark, Types.VARCHAR)
                .put("where id = ?", id, Types.INTEGER)
                .getResult();
        jdbcTemplate.update(buildResult.getSql(), buildResult.getParams(), buildResult.getTypes());
    }
}
