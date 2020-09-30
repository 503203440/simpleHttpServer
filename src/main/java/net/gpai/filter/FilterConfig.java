package net.gpai.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean CharacterEncoderFilter() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setOrder(0);
        filterRegistrationBean.setFilter(new CharacterEncoderFilter());
        filterRegistrationBean.addUrlPatterns("/icbcPay/b2bPayNotify/*");
        return filterRegistrationBean;

    }


}
