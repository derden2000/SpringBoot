package pro.antonshu.market.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class SoapConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "products")
    public DefaultWsdl11Definition productsWsdl11Definition(XsdSchema productsXsdSchema) {
        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();
        defaultWsdl11Definition.setPortTypeName("ProductsPort");
        defaultWsdl11Definition.setLocationUri("/ws");
        defaultWsdl11Definition.setTargetNamespace("http://market.antonshu.pro/soap/products");
        defaultWsdl11Definition.setSchema(productsXsdSchema);
        return defaultWsdl11Definition;
    }

    @Bean(name = "productsXsdSchema")
    public XsdSchema productsXsdSchema() {
        return new SimpleXsdSchema(new ClassPathResource("soap/products.xsd"));
    }
}
