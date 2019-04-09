package com.zzg.mybatis.generator.myplus.config;

/**
 * 自定义扩展配置
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 16:21
 */
public class ConfigManage {
    /**
     * 自定义扩展的开关
     */
    public static boolean isMyPlus = true;
    /**
     * 新增selectByEntity方法名称
     */
    public static String selectByEntityName = "selectByEntity";
    /**
     * 自定义的myTargetRuntime
     */
    public static String myTargetRuntime = "com.zzg.mybatis.generator.myplus.MyIntrospectedTable";
}
