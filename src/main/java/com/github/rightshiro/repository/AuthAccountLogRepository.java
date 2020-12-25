package com.github.rightshiro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rightshiro.model.entity.AuthAccountLogDO;

/**
 * @author weiguangchao
 * @date 2020/11/19
 */
@Repository
public interface AuthAccountLogRepository extends JpaRepository<AuthAccountLogDO, Long> {
}
