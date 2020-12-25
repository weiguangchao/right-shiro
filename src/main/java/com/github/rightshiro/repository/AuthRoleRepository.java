package com.github.rightshiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthRoleDO;

/**
 * @author weiguangchao
 * @date 2020/12/2
 */
@Repository
public interface AuthRoleRepository extends JpaRepository<AuthRoleDO, Long> {

    @Query("select role.code from AuthRoleDO role")
    String[] listAllRoleCode();
}
