package com.zzg.mybatis.generator.myplus;

import com.zzg.mybatis.generator.myplus.config.ConfigManage;
import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * ApplicationPlugin
 *
 * @Description
 * @Author HHJ
 * @Date 2019-09-27 14:20
 */
public class ApplicationPlugin extends PluginAdapter {
    private List<Method> methods = new ArrayList<>();

    private ShellCallback shellCallback = null;

    public ApplicationPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    private boolean isUseExample() {
        return "true".equals(getProperties().getProperty("useExample"));
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        boolean hasPk = introspectedTable.hasPrimaryKeyColumns();
        JavaFormatter javaFormatter = context.getJavaFormatter();

        String daoTargetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
        String daoTargetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        String javaFileEncoding = context.getProperty("javaFileEncoding");

        String entityNameNoEntity = ConfigManage.getDomainNameNoEntity(context);
        JavaClientGeneratorConfiguration mapperConfig = context.getJavaClientGeneratorConfiguration();
        String mapperPackage = mapperConfig.getTargetPackage();
        String applicationName = entityNameNoEntity + "Application";
        String applicationPackage = mapperPackage.substring(0, mapperPackage.lastIndexOf(".")) + "." + applicationName;
        FullyQualifiedJavaType type3 = new FullyQualifiedJavaType(applicationPackage);
        TopLevelClass mapperInterface = new TopLevelClass(type3);

        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.addImportedType("com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig");
            mapperInterface.addImportedType("org.mybatis.spring.annotation.MapperScan");
            mapperInterface.addImportedType("io.swagger.annotations.ApiOperation");
            mapperInterface.addImportedType("org.springframework.boot.autoconfigure.SpringBootApplication");
            mapperInterface.addImportedType("org.springframework.boot.builder.SpringApplicationBuilder");
            mapperInterface.addImportedType("org.springframework.cloud.client.discovery.EnableDiscoveryClient");
            mapperInterface.addImportedType("org.springframework.context.ConfigurableApplicationContext");
            mapperInterface.addImportedType("org.springframework.context.annotation.Bean");
            mapperInterface.addImportedType("springfox.documentation.swagger2.annotations.EnableSwagger2");
            mapperInterface.addImportedType("springfox.documentation.spring.web.plugins.Docket");
            mapperInterface.addImportedType("springfox.documentation.spi.DocumentationType");
            mapperInterface.addImportedType("springfox.documentation.builders.ApiInfoBuilder");
            mapperInterface.addImportedType("springfox.documentation.builders.RequestHandlerSelectors");

            mapperInterface.addAnnotation("@SpringBootApplication");
            mapperInterface.addAnnotation("@EnableDiscoveryClient");
            mapperInterface.addAnnotation("@EnableApolloConfig");
            mapperInterface.addAnnotation("@EnableSwagger2");
            mapperInterface.addAnnotation("@MapperScan(basePackages = {\"" + mapperPackage + "\"})");

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + entityNameNoEntity + " 服务运行程序");
            mapperInterface.addJavaDocLine(ConfigManage.getAuth());
            mapperInterface.addJavaDocLine(" */");

            getMain(applicationName, mapperInterface);
            getRestApi(applicationName, mapperInterface, entityNameNoEntity);


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

    private void getRestApi(String applicationName, TopLevelClass mapperInterface, String entityNameNoEntity) {
        Method method = new Method();
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Docket");
        method.setReturnType(returnType);
        method.setName("restApi");
        method.addAnnotation("@Bean");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().clear();
        List list = new ArrayList();
        list.add("return new Docket(DocumentationType.SWAGGER_2)\n" +
                "                    .apiInfo(new ApiInfoBuilder()\n" +
                "                            .title(\"" + entityNameNoEntity + "-服务接口\")\n" +
                "                            .description(\"" + entityNameNoEntity + "服务接口\")\n" +
                "                            .build())\n" +
                "                    .select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))\n" +
                "                    .build();");
        method.addBodyLines(list);
        mapperInterface.addMethod(method);

    }

    private void getMain(String applicationName, TopLevelClass mapperInterface) {
        Method method = new Method();
        method.setReturnType(null);
        method.setName("main");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.getParameters().clear();
        method.setStatic(true);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("String[]"), "args"));
        List list = new ArrayList();
        list.add("ConfigurableApplicationContext app = new SpringApplicationBuilder(" + applicationName + ".class).run(args);");
        list.add("System.out.println(app.getEnvironment().getProperty(\"spring.application.name\") + \"服务启动完毕...\");");
        method.addBodyLines(list);
        mapperInterface.addMethod(method);
    }


    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    private void interceptExampleParam(Method method) {
        if (isUseExample()) {
            method.getParameters().clear();
            method.addParameter(new Parameter(new FullyQualifiedJavaType("E"), "example"));
            methods.add(method);
        }
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
