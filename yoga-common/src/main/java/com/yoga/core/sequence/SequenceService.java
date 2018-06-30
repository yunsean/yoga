package com.yoga.core.sequence;

import com.yoga.core.data.BaseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;


@Service
public class SequenceService {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private SequenceRepository sequenceRepository;
	
	public long getNextValue(BaseEnum<String> name) {
		return getNextValue(name.getCode());
	}
	public long getNextValue(BaseEnum<String> name, long tenantId) {
		return getNextValue(name.getCode() + "_" + tenantId);
	}
	private long getNextValue(String name) {
		Object [] object = {name};
		Long result = jdbcTemplate.queryForObject("SELECT nextval(?) seq_val", object, long.class);
		if (result == null) {
			Long maxId = sequenceRepository.maxId();
			maxId = (maxId == null ? 0 : maxId) + 1;
			//这一段代码有点奇怪，使用seq_u_user_id添加的时候总是会失败，导致无法自动添加索引，所以改成JDBC访问
//			Sequence sequence = new Sequence();
//			sequence.setId(maxId);
//			sequence.setName(name.getCode());
//			sequence.setMax(100L);
//			sequence.setLength(12L);
//			sequence.setNext(1L);
//			sequenceRepository.save(sequence);
			String sql = "insert into g_sequence (length, max, name, next, rules, id) values (?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(sql, new Object[]{12L, 100L, name, 1L, null, maxId},
					new int[]{Types.BIGINT, Types.BIGINT, Types.VARCHAR, Types.BIGINT, Types.VARCHAR, Types.BIGINT});
			result = jdbcTemplate.queryForObject("SELECT nextval(?) seq_val", object, long.class);
		}
		return result;
	}
}
