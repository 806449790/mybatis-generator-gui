/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zzg.mybatis.generator.myplus;

import com.zzg.mybatis.generator.myplus.config.ConfigManage;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.TreeSet;

/**
 * 自定义的AbstractJavaMapperMethodGenerator
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 16:21
 */
public class MySelectByEntityJavaMethodGenerator extends AbstractJavaMapperMethodGenerator {
    private static final Logger log = LoggerFactory.getLogger(MySelectByEntityJavaMethodGenerator.class);

    public MySelectByEntityJavaMethodGenerator() {
        super();
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType;
        listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(ConfigManage.selectByEntityName);
        log.info("My JavaMethod Generator... Name:" + ConfigManage.selectByEntityName);
        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        //$NON-NLS-1$
        method.addParameter(new Parameter(parameterType, "entity"));

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        addMapperAnnotations(interfaze, method);

        if (context.getPlugins().clientSelectAllMethodGenerated(method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }
}
