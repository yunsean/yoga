package com.yoga.admin.aggregate.service;

import com.yoga.admin.aggregate.ao.Schedule;
import com.yoga.admin.aggregate.ao.Statement;
import com.yoga.admin.aggregate.ao.Todo;

import java.util.List;
import java.util.Set;

public interface AggregateActor {
    List<Schedule> getSchedules(long tenantId, long userId, Set<String> modules);
    List<Statement> getStatements(long tenantId, long userId, Set<String> modules);
    List<Todo> getTodos(long tenantId, long userId, Set<String> modules);
}
