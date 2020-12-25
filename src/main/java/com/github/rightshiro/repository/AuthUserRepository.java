package com.github.rightshiro.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthUserDO;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
@Repository
public interface AuthUserRepository extends JpaRepository<AuthUserDO, Long> {

    @Query("select user from AuthUserDO user where not exists ("
            + "select 1 from AuthUserRoleDO ur where user.uid=ur.userId and ur.roleId=?1 "
            + ")")
    Page<AuthUserDO> listNotAuthorityUserByRoleId(Long roleId, Pageable pageable);

    @Query("select user from AuthUserDO user inner join AuthUserRoleDO ur "
            + "where user.uid=ur.userId and ur.roleId=?1")
    Page<AuthUserDO> listUserByRoleId(Long roleId, Pageable pageable);

    AuthUserDO getByUid(String appleId);

    @Query("select role.code from AuthUserDO u "
            + "inner join AuthUserRoleDO ur on u.uid=ur.userId "
            + "inner join AuthRoleDO role on role.id=ur.roleId where u.uid=?1")
    String[] listUserRoleByUid(String appId);
}
