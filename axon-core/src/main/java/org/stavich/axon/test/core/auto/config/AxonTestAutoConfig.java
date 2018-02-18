package org.stavich.axon.test.core.auto.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.gateway.IntervalRetryScheduler;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ScheduledExecutorService;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty("axon.test.auto.config")
public class AxonTestAutoConfig {

    @Autowired
    private AxonConfiguration axonConfiguration;

    @Autowired
    private EventBus eventBus;

    @Bean
    public Serializer serializer(@Qualifier("jacksonObjectMapper") ObjectMapper objectMapper) {
        return new JacksonSerializer(objectMapper);
    }

    @Bean
    public CommandBusConnector commandBusConnector(@Qualifier("localSegment") SimpleCommandBus localSegment, Serializer serializer) {
        localSegment.registerDispatchInterceptor(new BeanValidationInterceptor<>());
        return new SpringHttpCommandBusConnector(localSegment, new RestTemplate(), serializer);
    }

    @Autowired
    public void configure(@Qualifier("localSegment") SimpleCommandBus simpleCommandBus) {
        simpleCommandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Bean
    public CommandGateway commandGateway(CommandBus commandBus, ScheduledExecutorService executorService) {
        IntervalRetryScheduler retryScheduler = new IntervalRetryScheduler(executorService, 5000, 5);
        return new DefaultCommandGateway(commandBus, retryScheduler, new MessageDispatchInterceptor[0]);
    }

}
