package com.yoga.imessager.friend.service;

import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.friend.model.Friend;
import com.yoga.imessager.friend.repo.FriendRepository;
import com.yoga.imessager.rongcloud.messages.TxtMessage;
import com.yoga.imessager.rongcloud.service.RongService;
import com.yoga.user.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

@Service(value = "imFriendService")
public class FriendService extends BaseService {

    @Autowired
    private RongService rongService;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserService userService;

    public void apply(long tenantId, long userId, long friendId, boolean allowMoment, String alias, String hello) {
        com.yoga.user.user.model.User from = userService.getUser(tenantId, userId);
        if (from == null) throw new BusinessException("用户不存在！");
        String fromName = StrUtil.isNotBlank(from.getFullname()) ? from.getFullname() : from.getPhone();
        TxtMessage message = new TxtMessage(hello, String.valueOf(userId));
        try {
            rongService.rongCloud(tenantId)
                    .message
                    .publishSystem(String.valueOf(userId), new String[]{String.valueOf(friendId)}, message, fromName + "想加你为好友！", null, 1, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("发送邀请失败");
        }
        Friend friend = new Friend(tenantId, userId, friendId, allowMoment, alias);
        friendRepository.save(friend);
    }
    @Transactional
    public void accept(long tenantId, long userId, long applyId, boolean allowMoment, String alias) {
        Friend friend = friendRepository.findByUserIdAndFriendId(applyId, userId);
        if (friend == null) throw new BusinessException("你没有收到对方的邀请！");
        friend.setAccepted(true);
        friendRepository.save(friend);
        friend = new Friend(tenantId, userId, applyId, allowMoment, alias);
        friend.setAccepted(true);
        friendRepository.save(friend);
    }
    public void reject(long tenantId, long userId, long applyId, String reason) {
        com.yoga.user.user.model.User from = userService.getUser(tenantId, userId);
        if (from == null) throw new BusinessException("用户不存在！");
        String fromName = StrUtil.isNotBlank(from.getFullname()) ? from.getFullname() : from.getPhone();
        Friend friend = friendRepository.findByUserIdAndFriendId(applyId, userId);
        if (friend == null) throw new BusinessException("你没有收到对方的邀请！");
        friendRepository.delete(friend);
        try {
            TxtMessage message = new TxtMessage(reason, String.valueOf(userId));
            rongService.rongCloud(tenantId)
                    .message
                    .publishSystem(String.valueOf(userId), new String[]{String.valueOf(applyId)}, message, fromName + "拒绝了你的好友申请！", null, 1, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException("发送邀请失败");
        }
    }
    public PageList<Friend> list(long tenantId, long userId, int pageIndex, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "addDate");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        return new PageList<>(friendRepository.findAll((Root<Friend> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();
            List<Expression<Boolean>> expressions = predicate.getExpressions();
            expressions.add(cb.equal(root.get("userId"), userId));
            expressions.add(cb.equal(root.get("tenantId"), tenantId));
            expressions.add(cb.equal(root.get("accepted"), true));
            expressions.add(cb.equal(root.get("blocked"), false));
            return predicate;
        }, pageable));
    }
    public void delete(long tenantId, long userId, long friendId) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId);
        if (friend == null || friend.getTenantId() != tenantId || !friend.isAccepted()) throw new BusinessException("对方不是你的好友！");
        friendRepository.delete(friendId);
    }
    public void modify(long tenantId, long userId, long friendId, Boolean allowMoment, String alias) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId);
        if (friend == null || friend.getTenantId() != tenantId || !friend.isAccepted()) throw new BusinessException("对方不是你的好友！");
        if (allowMoment != null) friend.setAllowMoment(allowMoment);
        if (alias != null) friend.setAlias(alias);
        friendRepository.save(friend);
    }
    public void block(long tenantId, long userId, long friendId, boolean block) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendId);
        if (friend == null || friend.getTenantId() != tenantId || !friend.isAccepted()) throw new BusinessException("对方不是你的好友！");
        friend.setBlocked(block);
        friendRepository.save(friend);
    }
}
