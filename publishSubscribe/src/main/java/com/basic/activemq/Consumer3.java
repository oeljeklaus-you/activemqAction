package com.basic.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * locate com.basic.activemq.p2p
 * Created by mastertj on 2018/5/14.
 * 消费者
 */
public class Consumer3 {
    //单列模式
    private static Consumer3 instance=null;

    //选择器
    private final String SELECTOR_2="age>50";

    private static synchronized Consumer3 getInstance(){
        if(instance==null){
            instance=new Consumer3();
        }
        return instance;
    }

    // 1.连接工厂
    private ConnectionFactory connectionFactory;

    // 2.连接对象
    private Connection connection;

    // 3.Session 对象
    private Session session;

    // 4.生产者
    private MessageConsumer messageConsumer;

    //5.目标地址
    private Destination destination;

    public Consumer3() {
        try {
            this.connectionFactory=new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER,
                    ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                    "tcp://ubuntu2:61616"
            );
            this.connection=connectionFactory.createConnection();
            connection.start();
            this.session=connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            this.destination=session.createTopic("PublishSubscribe");
            this.messageConsumer=session.createConsumer(destination);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 获取连接
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * 关闭连接
     */
    public void close(){
        try {
            if(connection!=null)
                connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void receiver() throws JMSException {
            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        if(message instanceof TextMessage){
                            TextMessage textMessage= (TextMessage) message;
                            System.out.println("consumer3:" +textMessage.getText());
                        }else if(message instanceof MapMessage){
                            MapMessage mapMessage= (MapMessage) message;
                            System.out.println(mapMessage.toString());
                        }
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public static void main(String[] args) throws JMSException {
        Consumer3 consumer= Consumer3.getInstance();
        consumer.receiver();
        System.out.println("-------------consumer3 over-------------");
    }
}
