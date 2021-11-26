package com.example.mall_study.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.example.mall_study.dto.WebLog;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制层统一日志处理切面
 * -@Aspect：用于定义切面
 * -@Before：前置通知，通知方法会在目标方法调用之前执行
 * -@After：后置通知，通知方法会在目标方法返回或抛出异常后执行
 * -@AfterReturning：返回通知，通知方法会在目标方法返回后执行
 * -@AfterThrowing：异常通知，通知方法会在目标方法抛出异常后执行
 * -@Around：环绕通知，通知方法会将目标方法封装起来
 * -@Pointcut：定义切点表达式
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 切入点
     * execution(方法修饰符 返回类型 方法所属的包.类名.方法名称(方法参数)
     */
    @Pointcut("execution(public * com.example.mall_study.controller.*.*(..))")
    public void webLog() {
    }

    /**
     * 前置通知
     * @param joinPoint 放回被代理的信息
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {

    }
    @After("webLog()")
    public void doAfter(JoinPoint joinPoint) throws Throwable{

    }

    /**
     * 返回通知
     * @param ret 返回值
     * @throws Throwable
     */
    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }

    /**
     * 环绕通知，在执行进入该方法，但还没有执行该方法的内容时执行
     * @param joinPoint 程序的动作控制
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息
        WebLog webLog = new WebLog();
        //继续执行被代理的方法，并获取到该方法的返回值
        Object result = joinPoint.proceed();
        //获取被代理的方法信息
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        //判断该方法是否有ApiOperation注解
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
            //获得注解的描述信息
            webLog.setDescription(apiOperation.value());
        }
        long endTime = System.currentTimeMillis();
        String urlStr = request.getRequestURL().toString();
        //获取请求该方法的请求地址
        webLog.setBasePath(StrUtil.removeSuffix(urlStr, URLUtil.url(urlStr).getPath()));
        webLog.setIp(request.getRemoteUser());
        //请求方式
        webLog.setMethod(request.getMethod());
        //方法参数
        webLog.setParameter(getParameter(method, joinPoint.getArgs()));
        //方法返回值
        webLog.setResult(result);
        //方法耗时
        webLog.setSpendTime((int) (endTime - startTime));
        //方法请求开始时间
        webLog.setStartTime(startTime);
        //方法请求URI
        webLog.setUri(request.getRequestURI());
        //方法请求URL
        webLog.setUrl(request.getRequestURL().toString());
        LOGGER.info("{}", JSONUtil.parse(webLog));
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }
}
