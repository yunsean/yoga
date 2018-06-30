package com.yoga.imessager.user.service;

import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.user.model.User;
import com.yoga.user.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Service(value = "imSystemUserService")
public class SystemUserService extends BaseService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    EntityManagerFactory entityManagerFactory;

    public PageList<User> findUsers(long tenantId, long groupId, String name, long departmentId, int pageIndex, int pageSize) {
        String condition = "from s_user u where u.tenant_id = ? and u.id not in (select gu.user_id from im_group_user gu where gu.group_id = ?)";
        if (StrUtil.isNotBlank(name)) condition += " and fullname like ?";
        if (departmentId != 0) condition += " and dept_id = ?";
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            int index = 0;
            Query query = em.createNativeQuery("select count(*) as count " + condition)
                    .setParameter(++index, tenantId)
                    .setParameter(++index, groupId);
            if (StrUtil.isNotBlank(name)) query.setParameter(++index, "%" + name + "%");
            if (departmentId != 0) query.setParameter(++index, departmentId);
            long count = ((Number)query.getSingleResult()).longValue();
            if (count < pageIndex * pageSize) return new PageList<>(new CommonPage(pageIndex, pageSize, count));
            index = 0;
            query = em.createNativeQuery("select u.* " + condition + " limit ?,?", User.class)
                    .setParameter(++index, tenantId)
                    .setParameter(++index, groupId);
            if (StrUtil.isNotBlank(name)) query.setParameter(++index, "%" + name + "%");
            if (departmentId != 0) query.setParameter(++index, departmentId);
            query.setParameter(++index, pageIndex * pageSize)
                    .setParameter(++index, pageSize);
            List<User> userList = query.getResultList();
            PageList<User> users = new PageList<>(userList, new CommonPage(pageIndex, pageSize, count));
            return users;
        } finally {
            em.close();
        }
    }
}
