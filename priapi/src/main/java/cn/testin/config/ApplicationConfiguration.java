package cn.testin.config;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class ApplicationConfiguration implements ResourceLoaderAware {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}


