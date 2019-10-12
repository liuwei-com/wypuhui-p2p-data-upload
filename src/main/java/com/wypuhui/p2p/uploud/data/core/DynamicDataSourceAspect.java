package com.wypuhui.p2p.uploud.data.core;

import com.wypuhui.p2p.uploud.data.annotation.MyDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author: liuw
 * @Date: 2019/10/11 17:21
 * @Description:
 */
@Aspect
@Slf4j
public class DynamicDataSourceAspect {

    @Pointcut("@within(com.wypuhui.p2p.uploud.data.annotation.MyDataSource) OR @annotation(com.wypuhui.p2p.uploud.data.annotation.MyDataSource))")
    public void sourcePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Before("sourcePointcut()")
    public void beforeSwitchDataSouce(JoinPoint point) {
        //获取当前访问的Class
//        Class<?> clazz = point.getTarget().getClass();
        Class<?> clazz = point.getSignature().getDeclaringType();
        //获取访问的方法名称
        String methodName = point.getSignature().getName();
        // 获取参数类型
        Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
        //获取默认数据库类型
        String dbType = DataSourceContextHolder.DEFAULT_DATASOURCE;
        log.info("解析访问对象信息，clazz : {} , methodName : {} , argClass[] : {} ", clazz.getName(), methodName, argClass);
        try {
            //判断是否存在DataSouce注解
            if (clazz.isAnnotationPresent(MyDataSource.class)) {
                MyDataSource db = clazz.getAnnotation(MyDataSource.class);
                dbType = db.value();
            }
        }catch (Exception e) {
            log.error("执行解析注解标有DataSource的类解析异常 ClassName : {} , methodName : {} ,argsClass[] : {} ",
                    clazz, methodName, argClass);
            log.error("异常栈信息 e ：{} ", e);
            throw new RuntimeException("解析方法注解异常");
        }
        log.info("当前执行sql数据源dbType : {} ", dbType);
        DataSourceContextHolder.setDataSource(dbType);
    }

    @After("sourcePointcut()")
    public void afterSwitchDataSource(JoinPoint point) {
        DataSourceContextHolder.clean();
        log.info("sql执行完毕，清理 DataSourceContextHolder 持有的数据源");
    }
}
