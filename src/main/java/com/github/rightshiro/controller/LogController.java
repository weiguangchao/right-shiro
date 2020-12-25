package com.github.rightshiro.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.rightshiro.model.entity.AuthAccountLogDO;
import com.github.rightshiro.model.entity.AuthOperationLogDO;
import com.github.rightshiro.model.vo.Message;
import com.github.rightshiro.model.vo.PageInfo;
import com.github.rightshiro.service.AccountLogService;
import com.github.rightshiro.service.OperationLogService;

import io.swagger.annotations.ApiOperation;

/**
 * @author weiguangchao
 * @date 2020/12/4
 */
@RestController
@RequestMapping("/log")
public class LogController {

    private AccountLogService accountLogService;
    private OperationLogService operationLogService;

    public LogController(AccountLogService accountLogService,
            OperationLogService operationLogService) {
        this.accountLogService = accountLogService;
        this.operationLogService = operationLogService;
    }

    @ApiOperation(value = "获取用户操作api日志列表", httpMethod = "GET")
    @RequestMapping("/operationLog/{page}/{size}")
    public Message getOperationLogList(@PathVariable Integer page, @PathVariable Integer size) {
        PageInfo<AuthOperationLogDO> pageInfo = new PageInfo<>(page, size);
        operationLogService.listAuthOperationLog(pageInfo);
        return new Message().ok(6666, "return operationLogList success").addData("data", pageInfo);
    }

    @ApiOperation(value = "获取日志记录", httpMethod = "GET")
    @RequestMapping("/accountLog/{page}/{size}")
    public Message getAccountLogList(@PathVariable Integer page, @PathVariable Integer size) {
        PageInfo<AuthAccountLogDO> pageInfo = new PageInfo<>(page, size);
        accountLogService.listAccountLog(pageInfo);
        return new Message().ok(6666, "return accountLogs success").addData("data", pageInfo);
    }
}
