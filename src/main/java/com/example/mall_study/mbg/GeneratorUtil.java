package com.example.mall_study.mbg;

import com.example.mall_study.component.JwtAuthenticationEntryPoint;
import io.micrometer.core.instrument.util.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneratorUtil.class);
    public static void build(){
        //MBG 执行过程中的警告信息
        List<String> warnings = new ArrayList<>();
        //当生成的代码重复时，是否覆盖原代码
        boolean overwrite = true;
        //读取我们的 MBG 配置文件（如果不想用配置文件，可以使用Context类用java代码写入配置config.addContext(context)即可）
        InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = null;
        try {
            config = cp.parseConfiguration(is);

            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            //创建MBG代码生成器
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            //执行生成代码（可以设置监听器）
            myBatisGenerator.generate(null);
            //输出警告信息
            for (String warning : warnings) {
                LOGGER.warn(warning);
            }
        } catch (IOException | InterruptedException | InvalidConfigurationException | SQLException |
                 XMLParserException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
