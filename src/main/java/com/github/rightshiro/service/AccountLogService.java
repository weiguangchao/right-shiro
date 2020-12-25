package com.github.rightshiro.service;

import java.util.List;

import com.github.rightshiro.model.entity.AuthAccountLogDO;
import com.github.rightshiro.model.vo.PageInfo;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
public interface AccountLogService {

    List<AuthAccountLogDO> listAccountLog(PageInfo<AuthAccountLogDO> pageInfo);
}
