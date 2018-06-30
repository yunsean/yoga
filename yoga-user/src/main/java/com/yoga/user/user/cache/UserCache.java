package com.yoga.user.user.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.user.user.dao.UserDAO;
import com.yoga.user.user.model.User;
import com.yoga.user.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends BaseCache {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserDAO userDAO;

    public PageList<User> findUserHasPrivilege(String[] privilege, String op, long tenantId, int pageIndex, int pageSize) {
        return userDAO.findUserWithPrivilege(privilege, op, tenantId, new CommonPage(pageIndex, pageSize));
    }
}
