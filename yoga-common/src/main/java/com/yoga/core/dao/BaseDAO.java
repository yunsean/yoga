package com.yoga.core.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseDAO {
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	Logger loggoer = Logger.getLogger(getClass());
}
