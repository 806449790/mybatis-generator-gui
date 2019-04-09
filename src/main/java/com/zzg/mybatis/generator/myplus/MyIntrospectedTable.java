package com.zzg.mybatis.generator.myplus;

import com.zzg.mybatis.generator.myplus.config.ConfigManage;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

import java.util.List;

/**
 * 重写默认的IntrospectedTableMyBatis3Impl
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 14:16
 */
public class MyIntrospectedTable extends IntrospectedTableMyBatis3Impl {


    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (ConfigManage.isMyPlus) {
            //添加自定义的 XMLMapperGenerator
            xmlMapperGenerator = new MyXMLMapperGenerator();
            initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
            return;
        }
        super.calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    @Override
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (ConfigManage.isMyPlus && context.getJavaClientGeneratorConfiguration() != null) {
            //添加自定义的 JavaMapperGenerator
            return new MyJavaMapperGenerator();
        }
        return super.createJavaClientGenerator();
    }
}