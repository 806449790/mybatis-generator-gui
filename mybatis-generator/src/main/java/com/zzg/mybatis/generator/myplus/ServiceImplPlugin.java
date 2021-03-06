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
 * ServiceImplPlugin
 *
 * @Description
 * @Author HHJ
 * @Date 2019-09-27 14:20
 */
public class ServiceImplPlugin extends PluginAdapter {
    private List<Method> methods = new ArrayList<>();

    private ShellCallback shellCallback = null;

    private static final FullyQualifiedJavaType LIST_TYPE = FullyQualifiedJavaType.getNewListInstance();
    public ServiceImplPlugin() {
        shellCallback = new DefaultShellCallback(false);
    }

    private boolean isUseExample() {
        return "true".equals(getProperties().getProperty("useExample"));
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        boolean hasPk = introspectedTable.hasPrimaryKeyColumns();
        JavaFormatter javaFormatter = context.getJavaFormatter();
        JavaClientGeneratorConfiguration mapperConfig = context.getJavaClientGeneratorConfiguration();
        String mapperPackage = mapperConfig.getTargetPackage();
        String daoTargetDir = context.getJavaClientGeneratorConfiguration().getTargetProject();
        String daoTargetPackage = context.getJavaClientGeneratorConfiguration().getTargetPackage();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<>();
        String javaFileEncoding = context.getProperty("javaFileEncoding");
        String domainNameNoEntity = ConfigManage.getDomainNameNoEntity(context);
        String entityPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        //自动注入的mapper变量名;
        String mapper = "mapper";
        String entityName = ConfigManage.getDomainObjectName(context);
        String serviceName = String.format(ConfigManage.DEFAULT_SERVICE, domainNameNoEntity);
        String serviceImplPackage = ConfigManage.getServiceImplPackage(daoTargetPackage, domainNameNoEntity);
        FullyQualifiedJavaType type3 = new FullyQualifiedJavaType(serviceImplPackage);
        TopLevelClass mapperInterface = new TopLevelClass(type3);
        FullyQualifiedJavaType baseExampleType = new FullyQualifiedJavaType(serviceName);
        mapperInterface.addSuperInterface(baseExampleType);
        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.addImportedType(LIST_TYPE);
            mapperInterface.addImportedType("org.springframework.stereotype.Service");
            mapperInterface.addImportedType("java.lang.Override");
            mapperInterface.addImportedType("org.springframework.beans.factory.annotation.Autowired");
            mapperInterface.addImportedType(mapperPackage + "." + domainNameNoEntity + "Mapper");
            mapperInterface.addImportedType(entityPackage + "." + entityName);
            mapperInterface.addAnnotation("@Service");
            mapperInterface.addImportedType(ConfigManage.getServicePackage(daoTargetPackage, domainNameNoEntity));
            FullyQualifiedJavaType srv = new FullyQualifiedJavaType("java.io.Serializable");
            mapperInterface.addImportedType(srv);

            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + serviceName + "实现类");
            mapperInterface.addJavaDocLine(ConfigManage.getAuth());
            if (isUseExample()) {
                mapperInterface.addJavaDocLine(" * " + "@param <E> The Example Class");
            }
            mapperInterface.addJavaDocLine(" */");
            Field field = new Field();
            field.addAnnotation("@Autowired");
            FullyQualifiedJavaType mapperJavaType = new FullyQualifiedJavaType(domainNameNoEntity + "Mapper");
            field.setType(mapperJavaType);
            field.setName(mapper);
            field.setVisibility(JavaVisibility.PRIVATE);
            mapperInterface.addField(field);

            addEntity(mapperInterface, mapper, entityName);
            selectByEntity(mapperInterface, mapper, entityName);

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

    private void addEntity(TopLevelClass mapperInterface, String mapper, String entityName) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Integer");
        method.setReturnType(returnType);
        method.setName("addEntity");
        method.addAnnotation("@Override");
        String entity = "entity";
        method.getParameters().clear();
        Parameter parameter = new Parameter(new FullyQualifiedJavaType(entityName), entity);
        method.addParameter(parameter);
        List list = new ArrayList();
        list.add("Integer res = " + mapper + ".insert(" + entity + ");");
        list.add("return res;");
        method.addBodyLines(list);
        mapperInterface.addMethod(method);
    }

    private void selectByEntity(TopLevelClass mapperInterface, String mapper, String entityName) {
        Method method = new Method();
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + entityName + ">");
        method.setReturnType(returnType);
        method.setName("selectByEntity");
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addAnnotation("@Override");
        String entity = "entity";
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType(entityName), entity));
        List list = new ArrayList();
        list.add("return " + mapper + ".selectByEntity(" + entity + ");");
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
