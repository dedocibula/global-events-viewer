package edu.vt.dlrl;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Author: dedocibula
 * Created on: 10.4.2017.
 */
@Configuration
public class ThymeleafConfiguration {

    private final ThymeleafProperties properties;

    @Autowired
    public ThymeleafConfiguration(ThymeleafProperties properties) {
        this.properties = properties;
    }

    @Bean
    public ITemplateResolver defaultTemplateResolver() {
        TemplateResolver resolver = new FileTemplateResolver();
        resolver.setSuffix(properties.getSuffix());
        resolver.setPrefix(properties.getPrefix());
        resolver.setTemplateMode(properties.getMode());
        resolver.setCharacterEncoding(properties.getEncoding().name());
        resolver.setCacheable(properties.isCache());
        return resolver;
    }

    @Bean
    public IDialect layoutDialect(){
        return new LayoutDialect();
    }
}
