package com.github.rightshiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthUserRoleDO;

/**
 * @author weiguangchao
 * @date 2020/11/25
 */
@Repository
public interface AuthUserRoleRepository extends JpaRepository<AuthUserRoleDO, Long> {

    AuthUserRoleDO findByUserIdAndRoleId(String uid, Long roleId);
}
