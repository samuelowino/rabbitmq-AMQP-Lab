package com.samlabs.rabbitlab;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitlabApplication {

    public static final String TIPS_EXCHANGE_NAME = "tips_exchange";
    public static final String DEFAULT_PARSING_QUEUE = "default_parsing_que";
    public static final String ROUTING_KEY = "tips_key";

    @Bean
    public TopicExchange tipsExchange(){
    	return  new TopicExchange(TIPS_EXCHANGE_NAME);
	}

	@Bean
	public Queue defaultParsingQueue(){
    	return new Queue(DEFAULT_PARSING_QUEUE);
	}

	@Bean
	public Binding queueToExchangeBinding(){
    	return BindingBuilder.bind(defaultParsingQueue()).to(tipsExchange()).with(ROUTING_KEY);
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter(){
    	return new Jackson2JsonMessageConverter();
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
    	RabbitTemplate rabbitTemplate = new RabbitTemplate();
    	rabbitTemplate.setConnectionFactory(connectionFactory);
    	rabbitTemplate.setMessageConverter(messageConverter());
    	return rabbitTemplate;
	}

    public static void main(String[] args) {
        SpringApplication.run(RabbitlabApplication.class, args);
    }

}
