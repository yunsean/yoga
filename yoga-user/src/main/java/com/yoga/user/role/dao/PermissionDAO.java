package com.yoga.user.role.dao;

import com.yoga.core.dao.BaseDAO;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.user.enums.AccreditObjectType;
import com.yoga.user.user.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Repository
public class PermissionDAO extends BaseDAO {

    public long getRoleUsedCount(long tenantId, long roleId) {
        String sql = "select count(*) from s_accredit where tenant_id = %d and role_id = %d";
        return jdbcTemplate.queryForObject(String.format(sql, tenantId, roleId), long.class);
    }

    @Transactional
    public void delDepartmentRole(long tenantId, long deptId) {
        String sql = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql, tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId));
    }

    @Transactional
    public void setDepartmentRoles(long tenantId, long deptId, Long[] roleIds) {
        String sql1 = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql1, tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId));
        if (roleIds == null || roleIds.length < 1) return;
        String sql2 = "insert into s_accredit (tenant_id, object_type, object_id, role_id) values ";
        Set<Long> roleIds1 = new HashSet<>();
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds1.contains(roleIds[i])) continue;
            roleIds1.add(roleIds[i]);
            if (i == 0)
                sql2 += String.format("(%d, %d, %d, %d)", tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId, roleIds[i]);
            else
                sql2 += String.format(", (%d, %d, %d, %d)", tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId, roleIds[i]);
        }
        jdbcTemplate.execute(sql2);
    }


    @Transactional
    public void setDepartmentRole(long tenantId, long deptId, long roleId) {
        String sql1 = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql1, tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId));
        String sql2 = "insert into s_accredit (tenant_id, object_type, object_id, role_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql2, new Object[]{tenantId, AccreditObjectType.DEPARTMENT.getCode(), deptId, roleId},
                new int[]{Types.BIGINT, Types.INTEGER, Types.BIGINT, Types.BIGINT});
    }

    @Transactional
    public void delDutyRoles(long tenantId, long dutyId) {
        String sql = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql, tenantId, AccreditObjectType.DUTY.getCode(), dutyId));
    }

    @Transactional
    public void setDutyRoles(long tenantId, long dutyId, long[] roleIds) {
        String sql1 = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql1, tenantId, AccreditObjectType.DUTY.getCode(), dutyId));
        if (roleIds == null || roleIds.length < 1) return;
        String sql2 = "insert into s_accredit (tenant_id, object_type, object_id, role_id) values ";
        Set<Long> roleIds1 = new HashSet<>();
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds1.contains(roleIds[i])) continue;
            roleIds1.add(roleIds[i]);
            if (i == 0)
                sql2 += String.format("(%d, %d, %d, %d)", tenantId, AccreditObjectType.DUTY.getCode(), dutyId, roleIds[i]);
            else
                sql2 += String.format(", (%d, %d, %d, %d)", tenantId, AccreditObjectType.DUTY.getCode(), dutyId, roleIds[i]);
        }
        jdbcTemplate.execute(sql2);
    }

    @Transactional
    public void delUserRoles(long tenantId, long userId){
        String sql = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql, tenantId, AccreditObjectType.USER.getCode(), userId));
    }

    @Transactional
    public void setUserRoles(long tenantId, long userId, long[] roleIds) {
        String sql1 = "delete from s_accredit where tenant_id = %d and object_type = %d and object_id = %d";
        jdbcTemplate.execute(String.format(sql1, tenantId, AccreditObjectType.USER.getCode(), userId));
        if (roleIds == null || roleIds.length < 1) return;
        String sql2 = "insert into s_accredit (tenant_id, object_type, object_id, role_id) values ";
        Set<Long> roleIds1 = new HashSet<>();
        for (int i = 0; i < roleIds.length; i++) {
            if (roleIds1.contains(roleIds[i])) continue;
            roleIds1.add(roleIds[i]);
            if (i == 0)
                sql2 += String.format("(%d, %d, %d, %d)", tenantId, AccreditObjectType.USER.getCode(), userId, roleIds[i]);
            else
                sql2 += String.format(", (%d, %d, %d, %d)", tenantId, AccreditObjectType.USER.getCode(), userId, roleIds[i]);
        }
        jdbcTemplate.execute(sql2);
    }

    public Set<String> getUserPermissions(User user) {
        if (user == null) return new HashSet<>();
        String sql = "select p.code as code from s_privilege p inner join s_accredit a on p.role_id = a.role_id and p.tenant_id = a.tenant_id where 1 = 0";
        String condition = " or a.tenant_id = %d and a.object_type = %d and a.object_id = %d";
        if (user.getDeptId() != null && user.getDeptId() != 0) {
            sql += String.format(condition, user.getTenantId(), AccreditObjectType.DEPARTMENT.getCode(), user.getDeptId());
        }
        if (user.getDutyId() != null && user.getDutyId() != 0) {
            sql += String.format(condition, user.getTenantId(), AccreditObjectType.DUTY.getCode(), user.getDutyId());
        }
        sql += String.format(condition, user.getTenantId(), AccreditObjectType.USER.getCode(), user.getId());
        return jdbcTemplate.query(sql, new ResultSetExtractor<Set<String>>() {
            @Override
            public Set<String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Set<String> permissions = new HashSet<String>();
                while (resultSet.next()) {
                    permissions.add(resultSet.getString("code"));
                }
                return permissions;
            }
        });
    }

    public Set<String> getRolePermissions(long tenantId, long roldId) {
        String sql = "select p.code as code from s_privilege p where p.tenant_id = %d and p.role_id = %d";
        return jdbcTemplate.query(String.format(sql, tenantId, roldId), new ResultSetExtractor<Set<String>>() {
            @Override
            public Set<String> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Set<String> permissions = new HashSet<String>();
                while (resultSet.next()) {
                    permissions.add(resultSet.getString("code"));
                }
                return permissions;
            }
        });
    }

    @Transactional
    public void setRolePermissions(long tenantId, long roleId, String[] permissions) {
        Set<String> unique = new HashSet<>();
        String sql1 = "delete from s_privilege where tenant_id = %d and role_id = %d";
        jdbcTemplate.execute(String.format(sql1, tenantId, roleId));
        String sql = "insert into s_privilege (tenant_id, role_id, code) values ";
        for (int i = 0; i < permissions.length; i++) {
            if (unique.contains(permissions[i])) continue;
            unique.add(permissions[i]);
            if (i == 0) sql += String.format("(%d, %d, '%s')", tenantId, roleId, permissions[i]);
            else sql += String.format(", (%d, %d, '%s')", tenantId, roleId, permissions[i]);
        }
        jdbcTemplate.execute(sql);
    }

    public List<MenuItem> getRoleTenantMenu(User user) {
        String sql = "select DISTINCT(m.id) as mid, m.* from g_tenant_menu m inner join s_privilege p on m.tenant_id = p.tenant_id and m.menu_code = p.code inner join s_accredit a on p.role_id = a.role_id and p.tenant_id = a.tenant_id where 1 = 0";
        String condition = " or a.tenant_id = %d and a.object_type = %d and a.object_id = %d";
        sql += String.format(condition, user.getTenantId(), AccreditObjectType.DEPARTMENT.getCode(), user.getDeptId());
        sql += String.format(condition, user.getTenantId(), AccreditObjectType.DUTY.getCode(), user.getDutyId());
        sql += String.format(condition, user.getTenantId(), AccreditObjectType.USER.getCode(), user.getId());
        return jdbcTemplate.query(sql, new ResultSetExtractor<List<MenuItem>>() {
            @Override
            public List<MenuItem> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                Map<String, List<MenuItem>> maps = new HashMap<>();
                while (resultSet.next()) {
                    int sort = resultSet.getInt("id");
                    String code = resultSet.getString("menu_code");
                    String name = resultSet.getString("name");
                    String url = resultSet.getString("url");
                    String remark = resultSet.getString("remark");
                    String group = resultSet.getString("menu_group");
                    List<MenuItem> items = maps.get(group);
                    if (items == null) maps.put(group, items = new ArrayList<>());
                    items.add(new MenuItem(sort, code, name, url, remark, false));
                }
                List<MenuItem> menuItems = new ArrayList<>();
                for (String key : maps.keySet()) {
                    menuItems.add(new MenuItem(key, maps.get(key)));
                }
                return menuItems;
            }
        });
    }
}
