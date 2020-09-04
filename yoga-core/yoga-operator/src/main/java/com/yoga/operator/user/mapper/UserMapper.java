package com.yoga.operator.user.mapper;

import com.yoga.core.mybatis.MyMapper;
import com.yoga.operator.role.model.Role;
import com.yoga.operator.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends MyMapper<User> {
    List<User> list(@Param("tenantId") long tenantId,
                    @Param("filter") String filter,
                    @Param("branchId") Long branchId,
                    @Param("dutyId") Long dutyId,
                    @Param("dutyCode") String dutyCode,
                    @Param("levelAbove") Integer levelAbove,
                    @Param("excludeBranchId") Long excludeBranchId);
    User get(@Param("tenantId") long tenantId,
             @Param("userId") long userId);
    User getByUserName(@Param("tenantId") long tenantId,
                       @Param("username") String username);
    /*  branchId 只查询本级，不递归父级和子级
        childBranchId 查询本级以及所有的父级，自动级联
     */
    List<User> listUserOfPrivilege(@Param("tenantId") long tenantId,
                                   @Param("filter") String filter,
                                   @Param("branchId") Long branchId,
                                   @Param("dutyId") Long dutyId,
                                   @Param("dutyCode") String dutyCode,
                                   @Param("levelAbove") Integer levelAbove,
                                   @Param("childBranchId") Long childBranchId,
                                   @Param("privilege") String privilege);
    List<User> listUserByChildBranchAndLessDuty(@Param("tenantId") long tenantId,
                                                @Param("branchId") Long branchId,
                                                @Param("dutyId") long dutyId);
    User getLowestDutyLevelUserByChildBranch(@Param("tenantId") long tenantId,
                                             @Param("branchId") long branchId,
                                             @Param("dutyId") long dutyId);       //在branchId及其所有子部门中获取比dutyId对应的职级更大的最低职级
    User getHighestDutyLevelUserByChildBranch(@Param("tenantId") long tenantId,
                                           @Param("branchId") long branchId);   //在branchId及其所有子部门中获取职级最高的用户
    List<User> listUserInIds(@Param("tenantId") long tenantId,
                             @Param("ids") List<Long> ids);
}
