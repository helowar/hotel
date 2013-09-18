package com.mangocity.hotel.base.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mangocity.mq.constant.MqConstants;
import com.mangocity.mq.hotel.JmsProvider;

/**
 * 
 * 处理本部触发的发lucene的mq消息
 * 
 * @author chenkeming
 *
 */
public class SendLuceneMQ {
	private static final int MAX_CONSUMER = 1;
	private static Log log = LogFactory.getLog(SendLuceneMQ.class);
	private BlockingQueue<String> queue;
	
    // 发送MQ功能
    private JmsProvider jmsProvider;

	public void init() {		
		queue = new LinkedBlockingQueue<String>();
		startConsumer();
	}
	
	public void send(String msg) {
		try {
			queue.put(msg);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void startConsumer() {
		ExecutorService service = Executors.newFixedThreadPool(MAX_CONSUMER);
		for (int i=0;i<MAX_CONSUMER;i++) {
			log.info("启动 Lucene CONSUMER " + i);
			service.execute(new Consumer("Lucene Consumer"+i));
		}
		service.shutdown();
	}
	
	private void process(String msg) {
    	try {
    		jmsProvider.sendTopic(MqConstants.TOPIC_HOTEL_LUCENE, msg);	
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
	}
	
	private class Consumer extends Thread {
		public Consumer(String name) {
			super(name);
		}

		@Override
		public void run() {
			while (true) {
				String msg = null;
				try {
					msg = queue.take();	
				} catch(InterruptedException e) {
					e.printStackTrace();
				} 
								
				if (null != msg) {
					process(msg);
					log.info(getName() + " 消息处理 : " + msg);
				}
				sleep();
			}
		}

		private void sleep() {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setJmsProvider(JmsProvider jmsProvider) {
		this.jmsProvider = jmsProvider;
	}

}

