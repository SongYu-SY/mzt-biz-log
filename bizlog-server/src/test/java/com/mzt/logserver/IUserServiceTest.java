package com.mzt.logserver;

import com.mzt.logapi.beans.LogRecord;
import com.mzt.logserver.infrastructure.constants.LogRecordType;
import com.mzt.logserver.infrastructure.logrecord.service.DbLogRecordService;
import com.mzt.logserver.pojo.Order;
import com.mzt.logserver.pojo.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wulang
 **/
public class IUserServiceTest extends BaseTest {
    @Resource
    private IUserService userService;
    @Resource
    private DbLogRecordService logRecordService;

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void diffUser() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("李四");
        newUser.setSex("女");
        newUser.setAge(20);
        User.Address newAddress = new User.Address();
        newAddress.setProvinceName("湖南省");
        newAddress.setCityName("长沙市");
        newUser.setAddress(newAddress);
        userService.diffUser(user, newUser);

        List<LogRecord> logRecordList = logRecordService.queryLog(String.valueOf(user.getId()), LogRecordType.USER);
        Assert.assertEquals(1, logRecordList.size());
        LogRecord logRecord = logRecordList.get(0);
        Assert.assertEquals(logRecord.getAction(), "更新了用户信息【address的cityName】从【武汉市】修改为【长沙市】；【address的provinceName】从【湖北省】修改为【湖南省】；【name】从【张三】修改为【李四】；【性别】从【男333】修改为【女333】");
        Assert.assertNotNull(logRecord.getExtra());
        Assert.assertEquals(logRecord.getOperator(), "111");
        logRecordService.clean();
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGlobalVariable() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);

        Order order = new Order();
        order.setOrderId(99L);
        order.setOrderNo("MT0000011");
        order.setProductName("超值优惠红烧肉套餐");
        order.setPurchaseName("张三");

        userService.testGlobalVariable(user, order);
        List<LogRecord> logRecordList = logRecordService.queryLog(String.valueOf(user.getId()), LogRecordType.USER);
        Assert.assertEquals(2, logRecordList.size());
        LogRecord userLogRecord = logRecordList.get(0);
        Assert.assertEquals(userLogRecord.getAction(), "更新张三用户积分信息");
        LogRecord orderLogRecord = logRecordList.get(1);
        Assert.assertEquals(orderLogRecord.getAction(), "更新用户张三的订单xxxx(99)信息,更新内容为...");
        logRecordService.clean();
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGlobalVariableCover() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);

        Order order = new Order();
        order.setOrderId(99L);
        order.setOrderNo("MT0000011");
        order.setProductName("超值优惠红烧肉套餐");
        order.setPurchaseName("张三");

        userService.testGlobalVariableCover(user, order);
        List<LogRecord> logRecordList = logRecordService.queryLog(String.valueOf(user.getId()), LogRecordType.USER);
        Assert.assertEquals(2, logRecordList.size());
        LogRecord userLogRecord = logRecordList.get(0);
        Assert.assertEquals(userLogRecord.getAction(), "更新张三用户积分信息");
        LogRecord orderLogRecord = logRecordList.get(1);
        Assert.assertEquals(orderLogRecord.getAction(), "更新用户李四的订单xxxx(99)信息,更新内容为...");
        logRecordService.clean();
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testAbstract() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);

        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("李四");
        newUser.setSex("女");
        newUser.setAge(20);
        User.Address newAddress = new User.Address();
        newAddress.setProvinceName("湖南省");
        newAddress.setCityName("长沙市");
        newUser.setAddress(newAddress);
        userService.testAbstract(user, newUser);

        List<LogRecord> logRecordList = logRecordService.queryLog(String.valueOf(user.getId()), LogRecordType.USER);
        Assert.assertEquals(1, logRecordList.size());
        LogRecord logRecord = logRecordList.get(0);
        Assert.assertEquals(logRecord.getAction(), "更新了用户信息【address的cityName】从【武汉市】修改为【长沙市】；【address的provinceName】从【湖北省】修改为【湖南省】；【name】从【张三】修改为【李四】；【性别】从【男333】修改为【女333】");
        Assert.assertNotNull(logRecord.getExtra());
        Assert.assertEquals(logRecord.getOperator(), "111");
        logRecordService.clean();
    }

    @Test
    @Sql(scripts = "/sql/clean.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void test_DIffLogIgnore_容器类失效() {
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        user.setSex("男");
        user.setAge(18);
        User.Address address = new User.Address();
        address.setProvinceName("湖北省");
        address.setCityName("武汉市");
        user.setAddress(address);
        List<String> likeList = new ArrayList<>();
        likeList.add("锅盔");
        likeList.add("热干面");
        likeList.add("豆皮");
        user.setLikeList(likeList);
        user.setTestList(Collections.singletonList(address));
        List<String> noLikeList = new ArrayList<>();
        noLikeList.add("蛙");
        noLikeList.add("鱼");
        user.setNoLikeList(noLikeList);
        user.setLikeStrings(new String[]{"a", "b", "c"});
        user.setNoLikeStrings(new String[]{"k", "p", "m"});

        User newUser = new User();
        newUser.setId(1L);
        newUser.setName("李四");
        newUser.setSex("女");
        newUser.setAge(20);
        User.Address newAddress = new User.Address();
        newAddress.setProvinceName("湖南省");
        newAddress.setCityName("长沙市");
        newUser.setAddress(newAddress);
        List<String> newLikeList = new ArrayList<>();
        newLikeList.add("臭豆腐");
        newLikeList.add("茶颜悦色");
        newUser.setLikeList(newLikeList);
        newUser.setTestList(Collections.singletonList(newAddress));
        List<String> newNoLikeList = new ArrayList<>();
        newNoLikeList.add("虾");
        newNoLikeList.add("龟");
        newUser.setNoLikeList(newNoLikeList);
        newUser.setLikeStrings(new String[]{"a", "p", "c"});
        newUser.setNoLikeStrings(new String[]{"k", "j", "u"});

        userService.diffUser(user, newUser);

        List<LogRecord> logRecordList = logRecordService.queryLog(String.valueOf(user.getId()), LogRecordType.USER);
        Assert.assertEquals(1, logRecordList.size());
        LogRecord logRecord = logRecordList.get(0);
        Assert.assertEquals(logRecord.getAction(), "更新了用户信息【address的cityName】从【武汉市】修改为【长沙市】；【address的provinceName】从【湖北省】修改为【湖南省】；【name】从【张三】修改为【李四】；【noLikeList】添加了【虾，龟】删除了【蛙，鱼】；【noLikeStrings】添加了【j，u】删除了【p，m】；【性别】从【男333】修改为【女333】");
        Assert.assertNotNull(logRecord.getExtra());
        Assert.assertEquals(logRecord.getOperator(), "111");
        logRecordService.clean();
    }
}
