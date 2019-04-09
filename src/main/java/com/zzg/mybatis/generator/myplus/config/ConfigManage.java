package com.zzg.mybatis.generator.myplus.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 自定义扩展配置
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 16:21
 */
public class ConfigManage {
    private static final Logger log = LoggerFactory.getLogger(ConfigManage.class);

    /**
     * 自定义扩展的开关
     */
    public static Boolean myPlusIfOpen = false;
    /**
     * 新增selectByEntity方法名称
     */
    public static String selectByEntityName = "selectByEntity";
    /**
     * 自定义的myTargetRuntime
     */
    public static String myTargetRuntime = "com.zzg.mybatis.generator.myplus.MyIntrospectedTable";
    /**
     * 配置文件
     */
    private static Properties prop;

    /**
     * 加载配置文件
     */
    private synchronized static void loadProps() {
        log.info("开始加载properties文件内容.......");
        prop = new Properties();
        InputStream in = null;
        try {
            /*第一种，通过类加载器进行获取properties文件流 */
            in = ConfigManage.class.getClassLoader().getResourceAsStream("myConfig.properties");
            prop.load(in);
        } catch (FileNotFoundException e) {
            log.error("config.properties文件未找到");
        } catch (IOException e) {
            log.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("config.properties文件流关闭出现异常");
            }
        }
        log.info("加载properties文件内容完成:" + prop);
    }

    /**
     * 开始配置信息
     */
    public static void start() {
        loadProps();
        myPlusIfOpen();
        myTargetRuntime();
    }

    /**
     * 自定义扩展类的全路径
     */
    private static void myTargetRuntime() {
        if (prop == null) {
            return;
        }
        String val = prop.getProperty("my.target.runtime");
        if (StringUtils.isNotEmpty(val)) {
            myTargetRuntime = val;
        }
    }

    /**
     * 是否开启自定义扩展
     */
    private static void myPlusIfOpen() {
        if (prop == null) {
            return;
        }
        String val = prop.getProperty("my.plus.if.open");
        if (Boolean.FALSE.toString().equals(val)) {
            myPlusIfOpen = false;
        }
        if (Boolean.TRUE.toString().equals(val)) {
            myPlusIfOpen = true;
        }
    }
}
