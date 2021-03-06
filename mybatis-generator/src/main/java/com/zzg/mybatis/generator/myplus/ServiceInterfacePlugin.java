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
 * ServiceInterfacePlugin
 *
 * @Description
 * @Author HHJ
 * @Date 2019-09-27 14:20
 */
public class ServiceInterfacePlugin extends PluginAdapter {
    private List<Method> methods = new ArrayList<>();

    private ShellCallback shellCallback = null;
    private static final FullyQualifiedJavaType LIST_TYPE = FullyQualifiedJavaType.getNewListInstance();

    public ServiceInterfacePlugin() {
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
        String entityPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();
        String servicePackage = ConfigManage.getServicePackage(daoTargetPackage, tableName);

        Interface mapperInterface = new Interface(servicePackage);
        String entityName = ConfigManage.getDomainObjectName(context);
        if (stringHasValue(daoTargetPackage)) {
            mapperInterface.setVisibility(JavaVisibility.PUBLIC);
            mapperInterface.addImportedType(LIST_TYPE);
            mapperInterface.addImportedType(new FullyQualifiedJavaType(entityPackage + "." + entityName));
            mapperInterface.addJavaDocLine("/**");
            mapperInterface.addJavaDocLine(" * " + "接口");
            mapperInterface.addJavaDocLine(ConfigManage.getAuth());
            if (isUseExample()) {
                mapperInterface.addJavaDocLine(" * " + "@param <E> The Example Class");
            }
            mapperInterface.addJavaDocLine(" */");
            addEntity(mapperInterface, entityName);
            selectByEntity(mapperInterface, entityName);

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

    private void addEntity(Interface mapperInterface, String entityName) {
        Method method = new Method();
        method.setConstructor(false);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("Integer");
        method.setReturnType(returnType);
        method.setName("addEntity");

        method.addJavaDocLine("/**\n" +
                "     * " + method.getName() + "\n" +
                "     *\n" +
                "     * @param entity\n" +
                "     * @return Integer\n" +
                "     */");
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType(entityName), "entity"));
        mapperInterface.addMethod(method);
    }

    private void selectByEntity(Interface mapperInterface, String entityName) {
        Method method = new Method();
        method.setConstructor(false);
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType("List<" + entityName + ">");
        method.setReturnType(returnType);
        method.setName("selectByEntity");
        method.addJavaDocLine("/**\n" +
                "     * " + method.getName() + "\n" +
                "     *\n" +
                "     * @param entity\n" +
                "     * @return List\n" +
                "     */");
        method.getParameters().clear();
        method.addParameter(new Parameter(new FullyQualifiedJavaType(entityName), "entity"));
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
