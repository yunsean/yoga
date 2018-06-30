package com.yoga.user.user.dao;

import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.utils.SqlBuilder;
import com.yoga.core.utils.SqlBuilder.BuildResult;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.user.enums.AccreditObjectType;
import com.yoga.user.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public PageList<User> findUserByParam(String name, Long deptId, CommonPage commonPage, long tenantId) {
		int pageIndex = commonPage.getPageIndex();
		int pageSize = commonPage.getPageSize();
		long count = findCountUserByParam(name, deptId, tenantId);
		TenantPage page = new TenantPage(pageIndex, pageSize, count);
		if(!page.hasRequestIndex()){
			return new PageList<User>();
		}
		BuildResult build = new SqlBuilder()
				.put("select u.*, d.name from s_user u left join s_department d on d.id = u.dept_id where 1 = 1 ")
				.put("and (u.fullname like ? or u.username like ?)", getQueryLikeParam(name), new int []{Types.VARCHAR, Types.VARCHAR})
				.put("and u.dept_id = ?", deptId != null ? deptId : null, Types.BIGINT)
				.put("and u.tenant_id = ?", tenantId, Types.BIGINT)
				.put("limit ?, ?", new Object[]{page.getPageIndex() * page.getPageSize(), pageSize}, new int[]{Types.INTEGER, Types.INTEGER})
				.getResult();
		List<User> userList = jdbcTemplate.query(build.getSql(), build.getParams(), build.getTypes(), new User());
		return new PageList<User>(userList, new CommonPage(commonPage.getPageIndex(), commonPage.getPageSize(), count));
	}
	
	private long findCountUserByParam(String name, Long deptId, long tenantId){
		BuildResult build = new SqlBuilder()
				.put("select count(u.id) from s_user u where 1 = 1 ")
				.put("and (u.fullname like ? or u.username like ?)", getQueryLikeParam(name), new int []{Types.VARCHAR, Types.VARCHAR})
				.put("and u.dept_id = ?", deptId != null ? deptId : null, Types.BIGINT)
				.put("and u.tenant_id = ?", tenantId, Types.BIGINT)
				.getResult();
		return jdbcTemplate.queryForObject(build.getSql(), build.getParams(), build.getTypes(), long.class);
	}
	
	private Object [] getQueryLikeParam(String name){
		if(StrUtil.isBlank(name)){
			return null;
		}
		return new Object[]{"%"+name+"%", "%"+name+"%"};
	}

	public PageList<User> findUserWithPrivilege(String[] privilege, String op, long tenantId, CommonPage commonPage) {
		int pageIndex = commonPage.getPageIndex();
		int pageSize = commonPage.getPageSize();
		long count = findCountUserWithPrivilege(privilege, op, tenantId);
		TenantPage page = new TenantPage(pageIndex, pageSize, count);
		if(!page.hasRequestIndex()){
			return new PageList<User>();
		}
		String condition = "(";
		List<Object> deptValues = new ArrayList<Object>(){{add(AccreditObjectType.DEPARTMENT.getCode());}};
		List<Object> dutyValues = new ArrayList<Object>(){{add(AccreditObjectType.DUTY.getCode());}};
		List<Object> userValues = new ArrayList<Object>(){{add(AccreditObjectType.USER.getCode());}};
		int[] types = new int[privilege.length + 2];
		types[0] = Types.INTEGER;
		types[privilege.length + 1] = Types.BIGINT;
		for (int i = 0; i < privilege.length; i++) {
			if (i != 0) condition += op;
			condition += " p.code = ? ";
			deptValues.add(privilege[i]);
			dutyValues.add(privilege[i]);
			userValues.add(privilege[i]);
			types[i + 1] = Types.VARCHAR;
		}
		condition += ")";
		deptValues.add(tenantId);
		dutyValues.add(tenantId);
		userValues.add(tenantId);

		BuildResult build = new SqlBuilder()
				.put("SELECT u.* FROM s_user u INNER JOIN s_department d ON u.dept_id = d.id INNER JOIN s_accredit a " +
								"ON d.id = a.object_id AND a.object_type = ? INNER JOIN s_role r ON a.role_id = r.id " +
								"INNER JOIN s_privilege p ON r.id = p.role_id WHERE " + condition +
								" AND p.tenant_id = ? ", deptValues.toArray(), types)
				.put("UNION " +
						"SELECT u.* FROM s_user u INNER JOIN s_duty t ON u.duty_id = t.id INNER JOIN " +
						"s_accredit a ON t.id = a.object_id AND a.object_type = ? INNER JOIN s_role r ON a" +
						".role_id = r.id INNER JOIN s_privilege p ON r.id = p.role_id WHERE " + condition +
						" AND p.tenant_id = ? ", dutyValues.toArray(), types)
				.put("UNION " +
						"SELECT u.* FROM s_user u INNER JOIN s_accredit a ON u.id = a.object_id AND a" +
						".object_type = ? INNER JOIN s_role r ON a.role_id = r.id INNER JOIN s_privilege p ON " +
						"r.id = p.role_id WHERE " + condition + " AND p.tenant_id = ?", userValues.toArray(), types)
				.put("limit ?, ?", new Object[]{page.getPageIndex() * page.getPageSize(), pageSize}, new int[]{Types.INTEGER, Types.INTEGER})
				.getResult();
				
		List<User> userList = jdbcTemplate.query(build.getSql(), build.getParams(), build.getTypes(), new User());
		return new PageList<User>(userList, new CommonPage(commonPage.getPageIndex(), commonPage.getPageSize(), count));
	}

	private long findCountUserWithPrivilege(String[] privilege, String op, long tenantId) {
		String condition = "(";
		List<Object> deptValues = new ArrayList<Object>(){{add(AccreditObjectType.DEPARTMENT.getCode());}};
		List<Object> dutyValues = new ArrayList<Object>(){{add(AccreditObjectType.DUTY.getCode());}};
		List<Object> userValues = new ArrayList<Object>(){{add(AccreditObjectType.USER.getCode());}};
		int[] types = new int[privilege.length + 2];
		types[0] = Types.INTEGER;
		types[privilege.length + 1] = Types.BIGINT;
		for (int i = 0; i < privilege.length; i++) {
			if (i != 0) condition += op;
			condition += " p.code = ? ";
			deptValues.add(privilege[i]);
			dutyValues.add(privilege[i]);
			userValues.add(privilege[i]);
			types[i + 1] = Types.VARCHAR;
		}
		condition += ")";
		deptValues.add(tenantId);
		dutyValues.add(tenantId);
		userValues.add(tenantId);
		BuildResult build = new SqlBuilder()
				.put("SELECT COUNT(*) FROM (SELECT u.id FROM s_user u INNER JOIN s_department d ON u.dept_id = d.id INNER JOIN s_accredit a " +
						"ON d.id = a.object_id AND a.object_type = ? INNER JOIN s_role r ON a.role_id = r.id INNER " +
						"JOIN s_privilege p ON r.id = p.role_id WHERE " + condition + " AND p.tenant_id = ? " +
						"UNION ", deptValues.toArray(), types)
				.put("SELECT u.id FROM s_user u INNER JOIN s_duty t ON u.duty_id = t.id INNER JOIN s_accredit a ON t" +
						".id = a.object_id AND a.object_type = ? INNER JOIN s_role r ON a.role_id = r.id INNER JOIN " +
						"s_privilege p ON r.id = p.role_id WHERE " + condition + " AND p.tenant_id = ? " +
						"UNION ", dutyValues.toArray(), types)
				.put("SELECT u.id FROM s_user u INNER JOIN s_accredit a ON u.id = a.object_id AND a.object_type = ? " +
						"INNER JOIN s_role r ON a.role_id = r.id INNER JOIN s_privilege p ON r.id = p.role_id WHERE " +
						condition + " AND p.tenant_id = ?) as ttt", userValues.toArray(), types)
				.getResult();
		return jdbcTemplate.queryForObject(build.getSql(), build.getParams(), build.getTypes(), long.class);
	}
}
