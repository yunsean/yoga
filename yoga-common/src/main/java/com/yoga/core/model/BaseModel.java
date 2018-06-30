package com.yoga.core.model;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseModel implements Serializable {
	private static final long serialVersionUID = 1L;
	public String toString() {
    	return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	protected <T> T fromResultSet(ResultSet rs, Class<T> classOfT) throws SQLException {
		try {
			@SuppressWarnings("unchecked")
			T item = (T) classOfT.newInstance();
			Field[] fields = classOfT.getDeclaredFields();
			for (Field f : fields) {
				try {
					String name = f.getName();
					String column = name;
					if (f.getAnnotation(Column.class) != null) {
						column = f.getAnnotation(Column.class).name();
					}
					Class<?> type = f.getType();
					Object value = rs.getObject(column);
					f.setAccessible(true);
					f.set(item, value);
				} catch (Exception e) {
				}
			}
			return item;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
