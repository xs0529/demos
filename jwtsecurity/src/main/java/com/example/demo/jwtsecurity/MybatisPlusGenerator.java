package com.example.demo.jwtsecurity;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus代码生成工具3.0版本
 * 详细配置：https://mp.baomidou.com/config/generator-config.html
 * @author xieshuang
 * 若使用请在maven添加以下依赖
 * <dependency>
 *             <groupId>org.apache.velocity</groupId>
 *             <artifactId>velocity</artifactId>
 *             <version>RELEASE</version>
 *         </dependency>
 */
public class MybatisPlusGenerator {

    public static void main(String[] args) {

        // 模块名
        String module = "jwtsecurity";
        // 是否多模块项目
        boolean ISMODULE = false;
        // 包路径
        String packAge = MybatisPlusGenerator.class.getPackage().getName();
        String PACKAGE1 = packAge+".system";
        // 作者
        String AUTHOR = "谢霜";
        // 数据库url
        String DATAURL = "127.0.0.1:3306";
        // 数据库用户名
        String USERNAME = "root";
        // 数据库密码
        String PASSWORD = "root";
        // 数据库名
        String DATABASE = "mydb";
        // 生成策略，ture：按照表名生成，false：按照表前缀生成
        Boolean B = false;
        // 需要生成的表
        String[] tableName = new String[] { "meeting", "user", "sign_record"};
        // 需要生成的表前缀
        String[] tableQ = new String[]{"sys_"};
        // 是否生成 Controller CRUD代码
        Boolean A = true;
        // 是否使用 lombok
        Boolean C = true;
        // 是否使用 Swagger
        Boolean D = false;
        // 是否使用 restController
        Boolean E = true;
        // 时间类型对应策略
        DateType dateType = DateType.TIME_PACK;


        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setDateType(dateType);
        gc.setOpen(false);
        gc.setSwagger2(D);
        String projectPath = System.getProperty("user.dir");
        if (ISMODULE){
            gc.setOutputDir(projectPath +"/"+ module +"/src/main/java");
        }else {
            gc.setOutputDir(projectPath + "/src/main/java");
        }
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(false);
        gc.setAuthor(AUTHOR);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setServiceName("%sService");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(USERNAME);
        dsc.setPassword(PASSWORD);
        dsc.setUrl("jdbc:mysql://"+DATAURL+"/"+DATABASE+"?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);全局大写命名 ORACLE 注意
        if (B){
            strategy.setInclude(tableName);
        }else {
            strategy.setTablePrefix(tableQ);
        }
        strategy.setEntityLombokModel(C);
        strategy.setRestControllerStyle(E);
        strategy.entityTableFieldAnnotationEnable(true);
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 表名生成策略
        strategy.setNaming(NamingStrategy.underline_to_camel);
        mpg.setStrategy(strategy);
        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        if (A){
            TemplateConfig tc = new TemplateConfig();
            tc.setController("/templates/generate/controller.java.vm");
            mpg.setTemplate(tc);
        }
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(PACKAGE1);
        mpg.setPackageInfo(pc);
        mpg.execute();
    }
}
