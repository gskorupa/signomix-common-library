package com.signomix.out.dispatcher;

import org.cricketmsf.exception.DispatcherException;
import java.util.HashMap;
import org.cricketmsf.Adapter;
import org.cricketmsf.event.Event;
import org.cricketmsf.out.OutboundAdapter;
import org.cricketmsf.out.dispatcher.DispatcherIface;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;
import org.cricketmsf.event.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Grzegorz Skorupa <g.skorupa at gmail.com>
 */
public class RabbitDispatcher extends OutboundAdapter implements Adapter, DispatcherIface {

    private static final Logger logger = LoggerFactory.getLogger(RabbitDispatcher.class);

    private String brokerURL;
    private String userName;
    private String password;
    private boolean debug = false;
    private String exchangeName = "events";
    private String eventTypes;
    private HashSet<String> eventMap;
    Channel channel;
    private boolean ready = false;

    @Override
    public void dispatch(Event event) throws DispatcherException {
        String eventClassName = event.getClass().getName();
        if (eventMap.contains(eventClassName)) {
            try {
                //channel.exchangeDeclare(exchangeName, "fanout");
                channel.basicPublish(exchangeName, eventClassName, null, new EventDto(event).toJson().getBytes("UTF-8"));
                logger.info("PUBLISHING EVENT");
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        } else {
            throw new DispatcherException(DispatcherException.UNKNOWN_EVENT);
        }
    }

    @Override
    public void loadProperties(HashMap<String, String> properties, String adapterName) {
        super.loadProperties(properties, adapterName);
        eventMap = new HashSet();
        brokerURL = properties.get("url");
        this.properties.put("url", brokerURL);
        logger.info("\turl: {}", brokerURL);
        userName = properties.get("user");
        this.properties.put("user", userName);
        logger.info("\tuser: {}", userName);
        password = properties.get("password");
        this.properties.put("password", password);
        logger.info("\tpassword: {}", password);
        eventTypes = properties.getOrDefault("events", "");
        try {
            exchangeName = properties.getOrDefault("exchange", "events");
            this.properties.put("exchange", exchangeName);
            logger.info("\texchange: {}", exchangeName);
            logger.info("\tevents: {}", eventTypes);
            logger.info("\tevents-configured: {}", eventMap.size());
        } catch (Exception e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public void registerEventTypes(String pathsConfig) {
        String[] paths = pathsConfig.split(";");
        for (String path : paths) {
            if (!path.isEmpty()) {
                eventMap.add(path);
            }
        }
    }

    @Override
    public DispatcherIface getDispatcher() {
        return this;
    }

    @Override
    public void start() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(brokerURL);
        factory.setUsername(userName);
        factory.setPassword(password);
        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, "fanout");
            registerEventTypes(eventTypes);
            /*
            Iterator it = eventMap.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                String name;
                int idx = key.lastIndexOf(".");
                if (idx >= 0) {
                    name = key.substring(idx + 1);
                } else {
                    name = key;
                }
                channel.queueDeclare(name, true, false, false, null);
                channel.queueBind(name, exchangeName, key);
            }
            */
            ready = true;
        } catch (IOException | TimeoutException ex) {
            logger.info(ex.getMessage());
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
