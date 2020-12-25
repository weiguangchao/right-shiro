package com.github.rightshiro.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.rightshiro.model.entity.AuthOperationLogDO;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.repository.AuthOperationLogRepository;
import com.github.rightshiro.exception.BaseRunException;
import com.github.rightshiro.service.OperationLogService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private AuthOperationLogRepository authOperationLogRepository;

    public OperationLogServiceImpl(
            AuthOperationLogRepository authOperationLogRepository) {
        this.authOperationLogRepository = authOperationLogRepository;
    }

    @Override
    public List<AuthOperationLogDO> listAuthOperationLog(PageInfo<AuthOperationLogDO> pageInfo) {
        Integer page = pageInfo.getPage();
        Integer size = pageInfo.getSize();
        Assert.notNull(page, "page is empty");
        Assert.notNull(size, "size is empty");

        try {
            Page<AuthOperationLogDO> authOperationLogDOPage = authOperationLogRepository.findAll(
                    PageRequest.of(page, size));

            pageInfo.setTotalPage(authOperationLogDOPage.getTotalPages());
            pageInfo.setTotalElement(authOperationLogDOPage.getTotalElements());

            List<AuthOperationLogDO> authOperationLogDOList = authOperationLogDOPage.getContent();
            if (CollectionUtil.isEmpty(authOperationLogDOList)) {
                pageInfo.setContent(authOperationLogDOList);
                return authOperationLogDOList;
            }

            return null;
        }
        catch (Exception ex) {
            new BaseRunException("获取操作日志失败::" + ex.getMessage()).process();
            return null;
        }
    }
}
