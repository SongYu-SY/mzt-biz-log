package com.mzt.logserver;

import com.mzt.logapi.starter.annotation.LogRecord;
import com.mzt.logserver.infrastructure.constants.LogRecordType;
import com.mzt.logserver.pojo.Order;
import com.mzt.logserver.pojo.User;

/**
 * @author wulang
 **/
public interface IUserService {
    boolean diffUser(User user, User newUser);

    boolean testGlobalVariable(User user, Order order);

    boolean testGlobalVariableCover(User user, Order order);

    @LogRecord(success = "更新了用户信息{_DIFF{#user, #newUser}}",
            type = LogRecordType.USER, bizNo = "{{#newUser.id}}",
            extra = "{{#newUser.toString()}}")
    boolean testAbstract(User user, User newUser);
}
