package com.commons.mq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * RocketMQ简单运用.
 * 初始化参数可以弄成配置形式.
 */
public class MQConsumer {

    private static DefaultMQPushConsumer consumer = null;


    public static void startCustomer() {
        try {
            /**
             * Consumer组名，多个Consumer如果属于一个应用，订阅同样的消息，且消费逻辑一致，则应该将它们归为同一组
             * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
             * 注意：ConsumerGroupName需要由应用来保证唯一
             */
            consumer = new DefaultMQPushConsumer("ConsumerGroupName");
            consumer.setNamesrvAddr("192.168.1.214:9876");
            consumer.setInstanceName("Consumber");

            /**
             * 订阅指定topic下tags分别等于TagA或TagB或TagC: TagA || TagB || TagC
             * * 表示订阅该topic所有tag
             *
             */
            consumer.subscribe("TopicTest11", "*");
            /**
             * 订阅指定topic下所有消息<br>
             * 注意：一个consumer对象可以订阅多个topic
             */
            /*consumer.subscribe("TopicTest12", "*");*/
            consumer.registerMessageListener(new MessageListenerConcurrently() {
                /**
                 * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
                 * consumeThreadMin:消费线程池数量 默认最小值10
                 * consumeThreadMax:消费线程池数量 默认最大值20
                 */
                public ConsumeConcurrentlyStatus consumeMessage(
                        List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (MessageExt msg : msgs) {
                        /**
                         * 可以根据不同的Topic 和 Tags执行不同的逻辑.. if else
                         */
                        System.out.println(msg.getTopic() + ": " + msg.getTags() + ": " + new String(msg.getBody()) + ": " + msg.getQueueId());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });

            /**
             * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             */
            consumer.start();

            System.out.println("Consumer Started.");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭consumer
     */
    public static void shutdownCustomer() {
        if(null != consumer) {
            consumer.shutdown();
        }
        System.out.println("Consumer Shutdown.");
    }
}