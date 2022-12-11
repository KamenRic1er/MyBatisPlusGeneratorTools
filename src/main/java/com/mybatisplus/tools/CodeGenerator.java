package com.mybatisplus.tools;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class CodeGenerator {

    //固定模板
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotBlank(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 1.全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "/src/main/java")  //代码最终输出的目录
                .setAuthor("少不入川")// 设置作者名字
                .setOpen(false)// 是否打开资源管理器
                .setFileOverride(true)// 是否覆盖原来生成的
                .setIdType(IdType.AUTO)// 主键策略
                .setBaseResultMap(true)// 生成resultMap
                .setDateType(DateType.ONLY_DATE) // 设置时间格式，采用Date
                .setServiceName("%sService");// 生成的service接口名字首字母是否为I，这样设置就没有I
        //setBaseColumnList(true)  XML中生成基础列

        // 2.数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl("jdbc:mysql://localhost:3306/itstudio?useUnicode=true&useSSL=false&characterEncoding=utf8" +
                "&serverTimezone" +
                "=Asia/Shanghai")
                .setDriverName("com.mysql.cj.jdbc.Driver")
                .setUsername("root")
                .setPassword("1017");

        // 3.包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.itstudio.service")
                .setController("controller") // Controller层
                .setEntity("pojo") // Pojo层
                .setMapper("mapper") // Mapper层
                .setService("service") // service层
                .setServiceImpl("service.impl"); // ServiceImp层

        // 4.策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel) //数据库表映射到实体的命名策略
                 .setColumnNaming(NamingStrategy.underline_to_camel) //数据库表字段映射到实体的命名策略
                 .setEntityLombokModel(true) // 是否使用Lombok
                 .setRestControllerStyle(true)  //restful api风格控制器
                 .setTablePrefix("t_") // 去除前缀
                 .setInclude(scanner("表名，多个英文逗号分割").split(","))
                 .setControllerMappingHyphenStyle(true);//是否生成实体时，生成字段注解


        // 5.自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }

        });
        cfg.setFileOutConfigList(focList);


        AutoGenerator autoGenerator = new AutoGenerator();// 构建代码生自动成器对象
        autoGenerator
                .setGlobalConfig(globalConfig)// 将全局配置放到代码生成器对象中
                .setDataSource(dataSourceConfig)// 将数据源配置放到代码生成器对象中
                .setPackageInfo(packageConfig)// 将包配置放到代码生成器对象中
                .setStrategy(strategyConfig)// 将策略配置放到代码生成器对象中
                .setCfg(cfg)// 将自定义配置放到代码生成器对象中
                .execute();// 执行！

    }

}