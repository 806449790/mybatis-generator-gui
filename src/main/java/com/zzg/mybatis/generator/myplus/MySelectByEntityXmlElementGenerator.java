package com.zzg.mybatis.generator.myplus;


import com.zzg.mybatis.generator.myplus.config.ConfigManage;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * 自定义的AbstractXmlElementGenerator
 *
 * @Description
 * @Author HHJ
 * @Date 2019-04-04 16:21
 */
public class MySelectByEntityXmlElementGenerator extends AbstractXmlElementGenerator {
    private static final Logger log = LoggerFactory.getLogger(MySelectByEntityXmlElementGenerator.class);

    public MySelectByEntityXmlElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$
        log.info("My XmlElement Generatoring... Name:"+ConfigManage.selectByEntityName);
        answer.addAttribute(new Attribute("id", ConfigManage.selectByEntityName)); //$NON-NLS-1$
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType.getFullyQualifiedName()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select "); //$NON-NLS-1$

        if (stringHasValue(introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,"); //$NON-NLS-1$
        }
        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(getBaseColumnListElement());
        if (introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(",")); //$NON-NLS-1$
            answer.addElement(getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from "); //$NON-NLS-1$
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement answerWhere = new XmlElement("where");
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            XmlElement notNullElement = new XmlElement("if"); //$NON-NLS-1$
            notNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$

            sb.setLength(0);
            sb.append(" and ");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            notNullElement.addElement(new TextElement(sb.toString()));
            answerWhere.addElement(notNullElement);
        }

        answer.addElement(answerWhere);
        parentElement.addElement(answer);
    }
}