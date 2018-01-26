package com.commons.mq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

/**
 * RocketMQ简单运用.
 * 初始化参数可以弄成配置形式.
 */
public class MQProducer {

    private static DefaultMQProducer producer = null;

    public static void startProducer() {
        try {
            /**
             * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
             * 注意：ProducerGroupName需要由应用来保证唯一<br>
             * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
             * 因为服务器会回查这个Group下的任意一个Producer
             */
            producer = new DefaultMQProducer("ProducerGroupName");
            producer.setNamesrvAddr("192.168.1.214:9876");

            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            producer.start();
            System.out.println("Producer Started.");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public static boolean sendMsg(String message) {
        try {
            Message msg = new Message("TopicTest11",// topic
                    "TagA",                         // tag
                    "OrderID001",                   // key
                    message.getBytes());    // body
            SendResult sendResult = producer.send(msg);  // orderID “10001”是传递给回调方法的自定义数据, 就是select方法中arg参数值
            //sendResult.getSendStatus().name()  SEND_OK or SEND_FAIL
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 关闭producer
     */
    public static void shutdownProducer() {
        /**
         * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
         * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
         */
        if(null != producer) {
            producer.shutdown();
        }
        System.out.println("Producer Shutdown.");
    }
}