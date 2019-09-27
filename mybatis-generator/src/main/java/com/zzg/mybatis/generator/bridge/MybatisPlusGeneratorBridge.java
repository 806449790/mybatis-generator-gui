package com.zzg.mybatis.generator.bridge;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zzg.mybatis.generator.model.DatabaseConfig;
import com.zzg.mybatis.generator.model.DbType;
import com.zzg.mybatis.generator.model.GeneratorConfig;
import com.zzg.mybatis.generator.util.DbUtil;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.ColumnOverride;
import org.mybatis.generator.config.IgnoredColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The bridge between GUI and the mybatis generator. All the operation to  mybatis generator should proceed through this
 * class
 * <p>
 /**
 * @Description
 * @Author HHJ
 * @Date 2019-09-27 14:20
 */
public class MybatisPlusGeneratorBridge {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlusGeneratorBridge.class);

    private static final String MAPPER_XML_FTL = "/templates/mapper.xml.ftl";

    private static final String AUTHOR_NAME = "HHJ";

    private GeneratorConfig generatorConfig;

    private DatabaseConfig databaseConfig;

    private ProgressCallback progressCallback;

    private List<IgnoredColumn> ignoredColumns;

    private List<ColumnOverride> columnOverrides;

    public MybatisPlusGeneratorBridge() {
    }

    public void generate() throws Exception {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = generatorConfig.getProjectFolder();
        String outputDir = projectPath + File.separator + generatorConfig.getModelPackageTargetFolder();
        gc.setOutputDir(outputDir);
        gc.setAuthor(AUTHOR_NAME);
        gc.setOpen(false);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        String dbType = databaseConfig.getDbType();
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(DbUtil.getConnectionUrlWithSchema(databaseConfig));
        // dsc.setSchemaName("public");
        dsc.setDriverName(DbType.valueOf(dbType).getDriverClass());
        dsc.setUsername(databaseConfig.getUsername());
        dsc.setPassword(databaseConfig.getPassword());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //scanner("模块名")
        pc.setModuleName("");
        String moduleParent = generatorConfig.getModelPackage().substring(0, generatorConfig.getModelPackage().lastIndexOf("."));
        pc.setParent(moduleParent);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(MAPPER_XML_FTL) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称
                return getMappingXMLFilePath(generatorConfig);
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        mpg.setTemplate(new TemplateConfig().setXml(null));

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        //strategy.setSuperEntityClass("com.baomidou.mybatisplus.samples.generator.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        // strategy.setSuperControllerClass("com.baomidou.mybatisplus.samples.generator.common.BaseController");
        strategy.setInclude(generatorConfig.getTableName());
        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        // 选择 freemarker 引擎需要指定如下加，注意 pom 依赖必须有！
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    public void setDatabaseConfig(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
    }

    private String getMappingXMLFilePath(GeneratorConfig generatorConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append(generatorConfig.getProjectFolder()).append(File.separator);
        sb.append(generatorConfig.getMappingXMLTargetFolder()).append(File.separator);
        String mappingXMLPackage = generatorConfig.getMappingXMLPackage();
        if (StringUtils.isNotEmpty(mappingXMLPackage)) {
            sb.append(mappingXMLPackage.replace(".", File.separator)).append(File.separator);
        }
        if (StringUtils.isNotEmpty(generatorConfig.getMapperName())) {
            sb.append(generatorConfig.getMapperName()).append(".xml");
        } else {
            sb.append(generatorConfig.getDomainObjectName()).append("Mapper.xml");
        }

        return sb.toString();
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    public void setIgnoredColumns(List<IgnoredColumn> ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
    }

    public void setColumnOverrides(List<ColumnOverride> columnOverrides) {
        this.columnOverrides = columnOverrides;
    }

}
