//package com.example.mall_study.common.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.JedisPool;
//
///**
// * jedisPool的配置
// */
//@Component
//public class JedisPoolConfig {
//
//    private JedisPoolConfig() {
//    }
//
//
//    //redis主机的ip
//    private static String host;
//
//    //redis主机的端口号
//    private static int port;
//    //登录口令
//    private static String auth;
//    /**
//     * 连接超时和读写超时（单位ms）
//     * 读写超时即：redis对该命令执行时间太长，超过设定时间后就放弃本次请求
//     */
//    private static int connAndReadWriteTimeOut;
//
//
//    /**
//     * 通过非静态的setter来给静态的属性赋值
//     *
//     * @param redisHost
//     */
//    @Value("${redis.host}")
//    public void setRedisHost(String redisHost) {
//        JedisPoolConfig.host = redisHost;
//    }
//
//    @Value("${redis.port}")
//    public void setRedisPort(int redisPort) {
//        JedisPoolConfig.port = redisPort;
//    }
//
//    @Value("${redis.auth}")
//    public void setAuth(String redisAuth) {
//        JedisPoolConfig.auth = redisAuth;
//    }
//
//    @Value("${redis.timeOut}")
//    public void setAuth(int timeOut) {
//        JedisPoolConfig.connAndReadWriteTimeOut = timeOut;
//    }
//
//
//    private static class innerClass {
//
//        //静态内部类保存一个jedisPool的实例
//        private static JedisPool jedisPool = new JedisPool(getConfig(), host, port, connAndReadWriteTimeOut, auth);
//
//        private static redis.clients.jedis.JedisPoolConfig getConfig() {
//            redis.clients.jedis.JedisPoolConfig poolConfig = new redis.clients.jedis.JedisPoolConfig();
//            //========= jedisPool的一些配置=============================
//            poolConfig.setMaxTotal(10000);//最大连接数
//            poolConfig.setMaxIdle(50);//最多空闲数
//            poolConfig.setMaxWaitMillis(5 * 1000);//当池中没有连接时，最多等待5秒
//            return poolConfig;
//        }
//    }
//
//    /**
//     * 返回jedisPool实例
//     *
//     * @return
//     */
//    public static JedisPool getJedisPool() {
//        return innerClass.jedisPool;
//    }
//
//}
