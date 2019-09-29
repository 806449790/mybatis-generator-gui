package com.zzg.mybatis.generator.myplus;

import com.zzg.mybatis.generator.myplus.config.ConfigManage;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * ControllerPlugin
 *
 * @Description
 * @Author HHJ
 * @Date 2019-09-27 14:20
 */
public class DataSourceConfigPlugin extends PluginAdapter {
    private ShellCallback shellCallback = null;

    public DataSourceConfigPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    private boolean isUseExample() {
        return "true".equals(getProperties().getProperty("useExample"));
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = context.getJavaFormatter();
        String daoTargetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
        String daoTargetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        String javaFileEncoding = context.getProperty("javaFileEncoding");
        String dataSourceConfigPackage = ConfigManage.getDataSourceConfigPackage(daoTargetPackage);
        FullyQualifiedJavaType classPackage = new FullyQualifiedJavaType(dataSourceConfigPackage);
        TopLevelClass mapperInterface = new TopLevelClass(classPackage);

        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.addImportedType("com.alibaba.druid.pool.DruidDataSource");
            mapperInterface.addImportedType("com.alibaba.druid.support.http.StatViewServlet");
            mapperInterface.addImportedType("com.alibaba.druid.support.http.WebStatFilter");
            mapperInterface.addImportedType("org.springframework.beans.factory.annotation.Value");
            mapperInterface.addImportedType("org.springframework.boot.web.servlet.FilterRegistrationBean");
            mapperInterface.addImportedType("org.springframework.boot.web.servlet.ServletRegistrationBean");
            mapperInterface.addImportedType("org.springframework.context.annotation.Bean");
            mapperInterface.addImportedType("org.springframework.context.annotation.Configuration");
            mapperInterface.addImportedType("lombok.extern.slf4j.Slf4j");

            mapperInterface.addImportedType("javax.sql.DataSource");
            mapperInterface.addImportedType("java.sql.SQLException");

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + "DruidDataSourceConfig");
            mapperInterface.addJavaDocLine(ConfigManage.getAuth());
            mapperInterface.addJavaDocLine(" */");
            mapperInterface.addAnnotation("@Slf4j");
            mapperInterface.addAnnotation("@Configuration");


            addField(mapperInterface, "${spring.datasource.url}", "String", "dbUrl");

            addField(mapperInterface, "${spring.datasource.username}", "String", "username");

            addField(mapperInterface, "${spring.datasource.password}", "String", "password");

            addField(mapperInterface, "${spring.datasource.driver-class-name}", "String", "driverClassName");

            addField(mapperInterface, "${spring.datasource.initialSize}", "int", "initialSize");

            addField(mapperInterface, "${spring.datasource.minIdle}", "int", "minIdle");

            addField(mapperInterface, "${spring.datasource.maxActive}", "int", "maxActive");

            addField(mapperInterface, "${spring.datasource.maxWait}", "int", "maxWait");

            addField(mapperInterface, "${spring.datasource.timeBetweenEvictionRunsMillis}", "int", "timeBetweenEvictionRunsMillis");

            addField(mapperInterface, "${spring.datasource.minEvictableIdleTimeMillis}", "int", "minEvictableIdleTimeMillis");

            addField(mapperInterface, "${spring.datasource.validationQuery}", "String", "validationQuery");

            addField(mapperInterface, "${spring.datasource.testWhileIdle}", "boolean", "testWhileIdle");

            addField(mapperInterface, "${spring.datasource.testOnBorrow}", "boolean", "testOnBorrow");

            addField(mapperInterface, "${spring.datasource.testOnReturn}", "boolean", "testOnReturn");

            addField(mapperInterface, "${spring.datasource.filters}", "String", "filters");

            addField(mapperInterface, "${spring.datasource.logSlowSql}", "String", "logSlowSql");


            /**
             * 配置访问页面信息
             * @return
             */
            getMethod(mapperInterface, "ServletRegistrationBean", "druidServlet", "ServletRegistrationBean reg = new ServletRegistrationBean();\n" +
                    "        reg.setServlet(new StatViewServlet());\n" +
                    "        reg.addUrlMappings(\"/druid/*\");\n" +
                    "        reg.addInitParameter(\"loginUsername\", username);\n" +
                    "        reg.addInitParameter(\"loginPassword\", password);\n" +
                    "        reg.addInitParameter(\"logSlowSql\", logSlowSql);\n" +
                    "        return reg;");
            getMethod(mapperInterface, "FilterRegistrationBean", "filterRegistrationBean", "\n" +
                    "        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();\n" +
                    "        filterRegistrationBean.setFilter(new WebStatFilter());\n" +
                    "        filterRegistrationBean.addUrlPatterns(\"/*\");\n" +
                    "        filterRegistrationBean.addInitParameter(\"exclusions\", \"*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*\");\n" +
                    "        filterRegistrationBean.addInitParameter(\"profileEnable\", \"true\");\n" +
                    "        return filterRegistrationBean;");
            getMethod(mapperInterface, "DataSource", "druidDataSource", "\n" +
                    "        DruidDataSource datasource = new DruidDataSource();\n" +
                    "        datasource.setUrl(dbUrl);\n" +
                    "        datasource.setUsername(username);\n" +
                    "        datasource.setPassword(password);\n" +
                    "        datasource.setDriverClassName(driverClassName);\n" +
                    "        datasource.setInitialSize(initialSize);\n" +
                    "        datasource.setMinIdle(minIdle);\n" +
                    "        datasource.setMaxActive(maxActive);\n" +
                    "        datasource.setMaxWait(maxWait);\n" +
                    "        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);\n" +
                    "        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);\n" +
                    "        datasource.setValidationQuery(validationQuery);\n" +
                    "        datasource.setTestWhileIdle(testWhileIdle);\n" +
                    "        datasource.setTestOnBorrow(testOnBorrow);\n" +
                    "        datasource.setTestOnReturn(testOnReturn);\n" +
                    "        try {\n" +
                    "            datasource.setFilters(filters);\n" +
                    "        } catch (SQLException e) {\n" +
                    "            log.error(\"druid configuration initialization filter\", e);\n" +
                    "        }\n" +
                    "        return datasource;");

            GeneratedJavaFile mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir, javaFileEncoding, javaFormatter);
            try {
                File mapperDir = shellCallback.getDirectory(daoTargetDir, daoTargetPackage);
                File mapperFile = new File(mapperDir, mapperJavafile.getFileName());
                // 文件不存在
                if (!mapperFile.exists()) {
                    mapperJavaFiles.add(mapperJavafile);
                }
            } catch (ShellException e) {
                e.printStackTrace();
            }
        }
        return mapperJavaFiles;
    }

    private void getMethod(TopLevelClass mapperInterface, String javaType, String name, String dodyLine) {
        Method method = new Method();
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(javaType);
        method.setReturnType(returnType);
        method.setName(name);
        method.addAnnotation("@Bean");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().clear();
        List list = new ArrayList();
        list.add(dodyLine);
        method.addBodyLines(list);
        mapperInterface.addMethod(method);

    }

    private void addField(TopLevelClass mapperInterface, String annotation, String javaType, String name) {
        Field field = new Field();
        field.addAnnotation("@Value(\"" + annotation + "\")");
        field.setType(new FullyQualifiedJavaType(javaType));
        field.setName(name);
        field.setVisibility(JavaVisibility.PRIVATE);
        mapperInterface.addField(field);
    }

    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    private void interceptExampleParam(Method method) {

    }

    private void interceptPrimaryKeyParam(Method method) {

    }

    private void interceptModelParam(Method method) {

    }

    private void interceptModelAndExampleParam(Method method) {

    }

    @Override
    public boolean clientCountByExampleMethodGenerated(Method method,
                                                       Interface interfaze, IntrospectedTable introspectedTable) {
//        interface
        if (isUseExample()) {
            interceptExampleParam(method);
        }
        return false;
    }


    @Override
    public boolean clientDeleteByExampleMethodGenerated(Method method,
                                                        Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptExampleParam(method);
        }
        return false;
    }


    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method,
                                                           Interface interfaze, IntrospectedTable introspectedTable) {
        interceptPrimaryKeyParam(method);
        return false;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze,
                                               IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithBLOBsMethodGenerated(Method method,
                                                                 Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptExampleParam(method);
            method.setReturnType(new FullyQualifiedJavaType("List<Model>"));
        }
        return false;
    }

    @Override
    public boolean clientSelectByExampleWithoutBLOBsMethodGenerated(Method method,
                                                                    Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptExampleParam(method);
            method.setReturnType(new FullyQualifiedJavaType("List<Model>"));
        }
        return false;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method,
                                                           Interface interfaze, IntrospectedTable introspectedTable) {
        interceptPrimaryKeyParam(method);
        method.setReturnType(new FullyQualifiedJavaType("Model"));
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method,
                                                                 Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptModelAndExampleParam(method);
        }
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithBLOBsMethodGenerated(Method method,
                                                                 Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptModelAndExampleParam(method);
        }
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method,
                                                                    Interface interfaze, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptModelAndExampleParam(method);
        }
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method,
                                                                    Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByExampleWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptModelAndExampleParam(method);
        }
        return false;
    }

    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (isUseExample()) {
            interceptModelAndExampleParam(method);
        }
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method,
                                                                    Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(
            Method method, Interface interfaze,
            IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        interceptModelParam(method);
        return false;
    }
}
