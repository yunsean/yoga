package com.yoga.core.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 消息生产者
 * @author Skysea
 *
 */
@Component
public class RabbitMqProducer {

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

	public RabbitMessagingTemplate getRabbitMessagingTemplate() {
		return rabbitMessagingTemplate;
	}

	public void setRabbitMessagingTemplate(RabbitMessagingTemplate rabbitMessagingTemplate) {
		this.rabbitMessagingTemplate = rabbitMessagingTemplate;
	}
}
