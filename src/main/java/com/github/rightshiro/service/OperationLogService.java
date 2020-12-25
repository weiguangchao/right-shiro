package com.github.rightshiro.service;

import java.util.List;

import com.github.rightshiro.model.entity.AuthOperationLogDO;
import com.github.rightshiro.model.vo.PageInfo;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
public interface OperationLogService {

    List<AuthOperationLogDO> listAuthOperationLog(PageInfo<AuthOperationLogDO> pageInfo);
}
