package com.github.rightshiro.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.rightshiro.model.entity.AuthAccountLogDO;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.repository.AuthAccountLogRepository;
import com.github.rightshiro.service.AccountLogService;
import com.github.rightshiro.exception.BaseRunException;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
@Service
public class AccountLogServiceImpl implements AccountLogService {

    private AuthAccountLogRepository authAccountLogRepository;

    public AccountLogServiceImpl(AuthAccountLogRepository authAccountLogRepository) {
        this.authAccountLogRepository = authAccountLogRepository;
    }

    @Override
    public List<AuthAccountLogDO> listAccountLog(PageInfo<AuthAccountLogDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthAccountLogDO> authAccountLogDOPage = authAccountLogRepository.findAll(PageRequest.of(page, size));

            pageInfo.setTotalPage(authAccountLogDOPage.getTotalPages());
            pageInfo.setTotalElement(authAccountLogDOPage.getTotalElements());

            List<AuthAccountLogDO> authAccountLogDOList = authAccountLogDOPage.getContent();
            if (CollectionUtil.isEmpty(authAccountLogDOList)) {
                pageInfo.setContent(authAccountLogDOList);
                return authAccountLogDOList;
            }

            return null;
        }
        catch (Exception ex) {
            new BaseRunException("获取用户日志失败::"+ex.getMessage()).process();
            return null;
        }
    }
}
