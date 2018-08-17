package cn.testin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.File;
import java.io.IOException;

/**
 * Created by xiejiuyang on 2017/10/18.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    /**
     * 启用文件服务
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String thisPath = null;
        //生成excel文件
        File directory = new File("");// 参数为空
        //要生成的文件路径
        try {
            thisPath = directory.getCanonicalPath() + "/tmp/";
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将网络请求映射到本地路径
        registry.addResourceHandler("/tmp/**").addResourceLocations("file:"+thisPath);
    }
}
