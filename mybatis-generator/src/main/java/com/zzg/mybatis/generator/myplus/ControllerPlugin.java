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
public class ControllerPlugin extends PluginAdapter {

    private List<Method> methods = new ArrayList<>();
    private static final FullyQualifiedJavaType LIST_TYPE = FullyQualifiedJavaType.getNewListInstance();
    private ShellCallback shellCallback = null;

    public ControllerPlugin() {
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
        String tableName = ConfigManage.getDomainNameNoEntity(context);
        String name = ConfigManage.getControllerPackage(daoTargetPackage, tableName);
        FullyQualifiedJavaType type3 = new FullyQualifiedJavaType(name);
        TopLevelClass mapperInterface = new TopLevelClass(type3);
        String domainNameNoEntity = ConfigManage.getDomainNameNoEntity(context);
        String entityPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        //自动注入的mapper变量名;
        String service = "service";
        String entityName = ConfigManage.getDomainObjectName(context);
        String serviceName = String.format(ConfigManage.DEFAULT_SERVICE, domainNameNoEntity);
        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.addImportedType(LIST_TYPE);
            mapperInterface.addImportedType("io.swagger.annotations.Api");
            mapperInterface.addImportedType("lombok.extern.slf4j.Slf4j");
            mapperInterface.addImportedType("io.swagger.annotations.ApiOperation");
            mapperInterface.addImportedType("org.springframework.web.bind.annotation.RestController");
            mapperInterface.addImportedType("org.springframework.web.bind.annotation.RequestMapping");
            mapperInterface.addImportedType("com.kanghe.ncdcloud.component.common.base.BaseResult");
            mapperInterface.addImportedType("org.springframework.beans.factory.annotation.Autowired");
            mapperInterface.addImportedType("org.springframework.http.MediaType");
            mapperInterface.addImportedType("io.swagger.annotations.ApiParam");
            mapperInterface.addImportedType("org.springframework.web.bind.annotation.RequestBody");
            mapperInterface.addImportedType("org.springframework.web.bind.annotation.RequestMethod");
            mapperInterface.addImportedType(ConfigManage.getServicePackage(daoTargetPackage, domainNameNoEntity));
            mapperInterface.addImportedType(entityPackage + "." + entityName);

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + "Controller类");
            mapperInterface.addJavaDocLine(ConfigManage.getAuth());
            if (isUseExample()) {
                mapperInterface.addJavaDocLine(" * " + "@param <E> The Example Class");
            }
            mapperInterface.addJavaDocLine(" */");
            mapperInterface.addAnnotation("@Slf4j");
            mapperInterface.addAnnotation("@Api(description = \"" + tableName + "\")");
            mapperInterface.addAnnotation("@RestController");
            char[] chars = tableName.toCharArray();
            chars[0] += 32;
            mapperInterface.addAnnotation("@RequestMapping(\"/" + String.valueOf(chars) + "\")");

            Field field = new Field();
            field.addAnnotation("@Autowired");
            FullyQualifiedJavaType mapperJavaType = new FullyQualifiedJavaType(serviceName);
            field.setType(mapperJavaType);
            field.setName(service);
            field.setVisibility(JavaVisibility.PRIVATE);
            mapperInterface.addField(field);

            addEntity(mapperInterface, service, entityName);
            selectByEntity(mapperInterface, service, entityName);


            List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
            for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles) {
                CompilationUnit compilationUnit = generatedJavaFile.getCompilationUnit();
                FullyQualifiedJavaType type = compilationUnit.getType();
                String modelName = type.getShortName();
                if (modelName.endsWith("DAO")) {
                }
            }
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
    private void addEntity(TopLevelClass mapperInterface, String service, String entityName) {
        Method method = new Method();
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("BaseResult<?>");
        method.setReturnType(returnType);
        method.setName("addEntity");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@ApiOperation(value = \"新增\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
        method.addAnnotation("@RequestMapping(value = \"/" + method.getName() + "\", method = RequestMethod.POST)");
        String entity = "entity";
        method.getParameters().clear();
        Parameter parameter = new Parameter(new FullyQualifiedJavaType(entityName), entity);
        parameter.addAnnotation("@ApiParam");
        parameter.addAnnotation("@RequestBody");
        method.addParameter(parameter);
        List list = new ArrayList();
        list.add("Integer res = " + service + "."+method.getName()+"(" + entity + ");");
        list.add("return BaseResult.buildSuccess(res);");
        method.addBodyLines(list);
        mapperInterface.addMethod(method);
    }

    private void selectByEntity(TopLevelClass mapperInterface, String service, String entityName) {
        Method method = new Method();
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("BaseResult<?>");
        method.setReturnType(returnType);
        method.setName("selectByEntity");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@ApiOperation(value = \"查询列表\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
        method.addAnnotation("@RequestMapping(value = \"/" + method.getName() + "\", method = RequestMethod.POST)");
        String entity = "entity";
        method.getParameters().clear();
        Parameter parameter = new Parameter(new FullyQualifiedJavaType(entityName), entity);
        parameter.addAnnotation("@ApiParam");
        parameter.addAnnotation("@RequestBody");
        method.addParameter(parameter);
        List list = new ArrayList();
        list.add("List<?> res = " + service + ".selectByEntity(" + entity + ");");
        list.add("return BaseResult.buildSuccess(res);");
        method.addBodyLines(list);
        mapperInterface.addMethod(method);
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
