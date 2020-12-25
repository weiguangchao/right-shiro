package com.github.rightshiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthRoleResourceDO;

/**
 * @author weiguangchao
 * @date 2020/11/27
 */
@Repository
public interface AuthRoleResourceRepository extends JpaRepository<AuthRoleResourceDO, Long> {

    void deleteByRoleIdAndResourceId(Long roleId, Long resourceId);
}
