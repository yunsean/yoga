package com.yoga.utility.journal.service;

import com.alibaba.fastjson.JSON;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.user.model.User;
import com.yoga.utility.journal.model.Journal;
import com.yoga.utility.journal.repo.JournalRepository;
import com.yoga.utility.sequence.SequenceNameEnum;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Service
public class JournalService extends BaseService {
    @Autowired
    private JournalRepository journalRepository;

    @PostConstruct
    private void postConstruct() {
        instance = this;
    }

    private static JournalService instance;
    public static void add(long tenantId, String module, String action) {
        String method;
        try {
            StackTraceElement[] stackTraces = new Throwable().getStackTrace();
            if (stackTraces != null && stackTraces.length > 1) {
                method = stackTraces[1].getClassName() + ":" + stackTraces[1].getMethodName();
            } else {
                method = "Unknown";
            }
        } catch (Exception ex) {
            method = "Unknown";
        }
        instance.add(tenantId, module, method, action);
    }
    public static void add2(long tenantId, String module, String action, String detail, Object source, Object target) {
        String method;
        try {
            StackTraceElement[] stackTraces = new Throwable().getStackTrace();
            if (stackTraces != null && stackTraces.length > 1) {
                method = stackTraces[1].getClassName() + ":" + stackTraces[1].getMethodName();
            } else {
                method = "Unknown";
            }
        } catch (Exception ex) {
            method = "Unknown";
        }
        instance.add2(tenantId, module, method, action, detail, source, target);
    }
    public static void add(long tenantId, String module, String action, String detail, Object... paramses) {
        String method;
        try {
            StackTraceElement[] stackTraces = new Throwable().getStackTrace();
            if (stackTraces != null && stackTraces.length > 1) {
                method = stackTraces[1].getClassName() + ":" + stackTraces[1].getMethodName();
            } else {
                method = "Unknown";
            }
        } catch (Exception ex) {
            method = "Unknown";
        }
        instance.add3(tenantId, module, method, action, detail, paramses);
    }

    public void add(long tenantId, String module, String method, String action) {
        add3(tenantId, module, method, action, null);
    }
    public void add2(long tenantId, String module, String method, String action, String detail, Object source, Object target) {
        if (detail == null) detail = "";
        detail += " 从 ";
        if (source == null) detail += "[null]";
        else detail += JSON.toJSONString(source);
        source += " 到 ";
        if (target == null) detail += "[null]";
        else detail += JSON.toJSONString(target);
        add3(tenantId, module, method, action, detail);
    }
    public void add3(long tenantId, String module, String method, String action, String detail, Object... paramses) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        long userId;
        String userName;
        if (user == null) {
            userId = 0;
            userName = "Anonymous";
        } else {
            userId = user.getId();
            userName = user.getFullname();
        }
        Journal journal = new Journal(tenantId, module, method, action, userId, userName, detail + getParams(paramses));
        journal.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_U_JOURNAL_ID));
        journalRepository.save(journal);
    }
    private static StringBuffer getParams(Object... params) {
        StringBuffer strParmas = new StringBuffer();
        boolean isFirt = true;
        if(params!=null){
            for(Object param: params){
                if(isFirt){
                    if(param!=null){
                        strParmas.append("["+param.toString()+"]");
                    }else{
                        strParmas.append("[null]");
                    }
                    isFirt = false;
                }else{
                    if(param!=null){
                        strParmas.append(",["+param.toString()+"]");
                    }else{
                        strParmas.append(",[null]");
                    }
                }
            }
        }
        return strParmas;
    }

    public PageList<Journal> find(long tenantId, Date begin, Date end, String user, String module, String action, String method, int pageIndex, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return new PageList<>(journalRepository.findAll((Root<Journal> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.get("tenantId"), tenantId));
            if (begin != null) expressions.add(cb.greaterThanOrEqualTo(root.get("time"), begin));
            if (end != null) expressions.add(cb.lessThanOrEqualTo(root.get("time"), end));
            if (StrUtil.isNotBlank(user)) expressions.add(cb.like(root.get("user"), "%" + user + "%"));
            if (StrUtil.isNotBlank(module)) expressions.add(cb.like(root.get("module"), "%" + module + "%"));
            if (StrUtil.isNotBlank(action)) expressions.add(cb.like(root.get("action"), "%" + action + "%"));
            if (StrUtil.isNotBlank(method)) expressions.add(cb.like(root.get("method"), "%" + method + "%"));
            return predicate;
        }, pageable));
    }

    public Journal get(long tenantId, long id) {
        Journal journal = journalRepository.findOne(id);
        if (journal == null || journal.getTenantId() != tenantId) throw new BusinessException("未找到该日志！");
        return journal;
    }
}
