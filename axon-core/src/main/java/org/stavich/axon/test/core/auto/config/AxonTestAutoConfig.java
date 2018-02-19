package org.stavich.axon.test.core.auto.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;

@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty("axon.test.auto.config")
public class AxonTestAutoConfig {

    @Autowired
    private AxonConfiguration axonConfiguration;

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

    @Autowired
    public void configure(EventHandlingConfiguration config) {
        config.usingTrackingProcessors(); // default all processors to tracking mode.
    }

    // TODO - Implement Command Retries
//    @Bean
//    public CommandGateway commandGateway(CommandBus commandBus, ScheduledExecutorService executorService) {
//        IntervalRetryScheduler retryScheduler = new IntervalRetryScheduler(executorService, 5000, 5);
//        return new DefaultCommandGateway(commandBus, retryScheduler, new MessageDispatchInterceptor[0]);
//    }

}
