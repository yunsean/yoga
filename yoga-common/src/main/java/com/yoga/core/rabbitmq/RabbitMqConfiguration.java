package com.yoga.core.rabbitmq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
/**
 * MQ消息配置
 * @author Skysea
 *
 */
public abstract class RabbitMqConfiguration implements RabbitListenerConfigurer {

	protected abstract String getExchangeName();
	protected abstract String [] getQueueNames();
    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }
    

    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    
	@Bean
	DirectExchange bindQueueToExchange(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        DirectExchange exchange = exchange(admin);
        admin.declareExchange(exchange);
        String[] queueNames = getQueueNames();
        for (String queueName : queueNames) {
        	Queue queue = new Queue(queueName);
        	admin.declareQueue(queue);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName);
            admin.declareBinding(binding);
        }
        
        return exchange;
	}

    @Bean
    DirectExchange exchange(RabbitAdmin rabbitAdmin) {
    	DirectExchange topicExchange = new DirectExchange(getExchangeName());
        rabbitAdmin.declareExchange(topicExchange);
        return topicExchange;
    }
    
    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(jackson2Converter());
        rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
        return rabbitMessagingTemplate;
    }
    
    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }
}
