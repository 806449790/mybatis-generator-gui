package com.zzg.mybatis.generator.myplus;

import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * 自定义的XMLMapperGenerator
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 14:18
 */
public class MyXMLMapperGenerator extends XMLMapperGenerator {

    public MyXMLMapperGenerator() {
        super();
    }

    @Override
    protected XmlElement getSqlMapElement() {
        XmlElement answer = super.getSqlMapElement();
        addSelectByConditionElement(answer);
        return answer;
    }


    protected void addSelectByConditionElement(
            XmlElement parentElement) {
        if (introspectedTable.getRules().generateSelectByPrimaryKey()) {
            AbstractXmlElementGenerator elementGenerator = new MySelectByEntityXmlElementGenerator();
            initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }
}