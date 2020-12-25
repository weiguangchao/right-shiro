package com.github.rightshiro.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;

/**
 * @author weiguangchao
 * @date 2020/11/18
 */
public class StatelessWebSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        // 始终不创建session
        context.setSessionCreationEnabled(Boolean.FALSE);
        return super.createSubject(context);
    }
}
