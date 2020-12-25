package com.github.rightshiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthOperationLogDO;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
@Repository
public interface AuthOperationLogRepository extends JpaRepository<AuthOperationLogDO, Long> {
}
