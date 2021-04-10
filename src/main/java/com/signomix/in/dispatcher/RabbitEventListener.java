package com.signomix.in.dispatcher;

/**
 *
 * @author greg
 */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import org.cricketmsf.Adapter;
import org.cricketmsf.event.Event;
import org.cricketmsf.event.EventDto;
import org.cricketmsf.in.InboundAdapter;
import org.cricketmsf.in.event.EventHandler;
import org.cricketmsf.in.event.EventListenerIface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RabbitEventListener extends InboundAdapter implements Adapter, EventListenerIface {

    private static final Logger logger = LoggerFactory.getLogger(RabbitEventListener.class);

    private String brokerURL;
    private String userName;
    private String password;
    private String exchangeName;
    private String eventName;
    Channel channel;
    private boolean ready = false;

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        super.loadProperties(properties, adapterName);
        brokerURL = properties.get("url");
        this.properties.put("url", brokerURL);
        logger.info("\turl: {}", brokerURL);
        userName = properties.get("user");
        this.properties.put("user", userName);
        logger.info("\tuser: {}", userName);
        password = properties.get("password");
        this.properties.put("password", password);
        logger.info("\tpassword: {}", password);
        try {
            exchangeName = properties.getOrDefault("exchange", "events");
            this.properties.put("exchange", exchangeName);
            logger.info("\texchange: {}", exchangeName);
            eventName = properties.getOrDefault("event", "");
            //registerEventTypes(eventTypes);
            logger.info("\tevents: {}", eventName);
            //logger.info("\tevent-types-configured: {}", eventMap.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(brokerURL);
            factory.setUsername(userName);
            factory.setPassword(password);
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "fanout");
            channel.queueBind(eventName, exchangeName, eventName);
            //channel.exchangeDeclare(exchangeName, "direct");
            //String queueName = channel.queueDeclare().getQueue();
            //channel.queueBind(queueName, exchangeName, "");
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String serializedEvent = new String(delivery.getBody(), "UTF-8");
                try {
                    EventDto edto = EventDto.fromJson(serializedEvent);
                    Event event = (Event) Class.forName(edto.eventClassName).newInstance();
                    new Thread(
                            new EventHandler(event)
                    ).start();
                } catch (ClassCastException ex) {
                    //probably event from signomix version 1
                    logger.warn("event from signomix v1 can't be handled: {}", ex.getMessage());
                    logger.warn("problem with {}",serializedEvent);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    logger.error(ex.getMessage());
                    logger.warn("problem with {}",serializedEvent);
                }
            };
            channel.basicConsume(eventName, true, deliverCallback, consumerTag -> {
            });
            ready = true;
        } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void destroy() {
        try {
            channel.close();
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
        } catch (TimeoutException ex) {
            logger.warn(ex.getMessage());
        }
    }
}
