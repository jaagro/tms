package com.jaagro.tms;

import com.jaagro.tms.api.service.OcrService;
import com.jaagro.tms.biz.service.impl.OcrServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.ws.rs.core.Application;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TmsApplicationTests {

    @Autowired
    OcrService ocrService;

    @Test
    public void contextLoads() {
    }

    /**
     * Mybatis逆向生成测试方法
     * @throws Exception
     */
    @Test
    public void mybatisGenerator() throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("E:\\git\\jaagro-platform-tms\\tms-biz\\src\\main\\resources\\mybatis\\generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }

    @Test
    public void ocrTest(){
        ocrService.getOcrByMuYuanAppImage();
    }

}
