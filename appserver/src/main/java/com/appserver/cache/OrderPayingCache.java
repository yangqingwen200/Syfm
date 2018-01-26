package com.appserver.cache;

import com.google.common.collect.Maps;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public final class OrderPayingCache {

	private final static Logger LOG = LoggerFactory.getLogger(OrderPayingCache.class);

	private final ScheduledExecutorService SES
			= Executors.newScheduledThreadPool(16, new DefaultThreadFactory("OrderPayingFactory"));
	private final ConcurrentMap<String, ScheduledFuture<?>> ORDER_FUTURE = Maps.newConcurrentMap();
	private final static OrderPayingCache INSTANCE = new OrderPayingCache();
	private final static String TO_DISK_FILE = "order_paying_task.cache";
	private final static String ORDERID_DELAY_SPLIT = ",";

	private OrderPayingCache() {
	}

	public static OrderPayingCache getInstance() {
		return INSTANCE;
	}

	public ScheduledFuture<?> schedule(final String orderNumber, final int delayMinutes) {
		if (delayMinutes <= 0)
			return null;
		ScheduledFuture<?> sf = SES.schedule(() -> {
			try {
				LOG.info("Order pay timeout. orderNumber:{}", orderNumber);
				System.out.println(orderNumber);
				/*PassengerOrderService passengerOrderService = AppContext.getBean(PassengerOrderService.class);
				passengerOrderService.orderPayTimeout(orderNumber);*/
				cancel(orderNumber, false);
			} catch (Throwable e) {
				LOG.error("OrderNumber play timeout, cancel order exception.", e);
			}
		}, delayMinutes, TimeUnit.MINUTES);
		if (sf.isDone() || sf.isCancelled()) {
			return sf;
		}
		ScheduledFuture<?> oldSf = ORDER_FUTURE.putIfAbsent(orderNumber, sf);
		return oldSf == null ? sf : oldSf;
	}

	public long remainPayingSecondsByOrderId(String orderNumber) {
		ScheduledFuture<?> sf = ORDER_FUTURE.get(orderNumber);
		if (sf == null) {
			return 0l;
		}
		return sf.getDelay(TimeUnit.SECONDS);
	}

	public List<String> getOrderPayingRemainTimes() {
		List<String> orders = new ArrayList<>();
		ScheduledFuture<?> sf;
		for (String orderNumber : ORDER_FUTURE.keySet()) {
			sf = ORDER_FUTURE.get(orderNumber);
			orders.add(orderNumber + ORDERID_DELAY_SPLIT + sf.getDelay(TimeUnit.MINUTES));
		}
		return orders;
	}

	public void loadContinueTask() {
		LOG.info("OrderPayingCache initialize the previously saved data");
		File file = new File(TO_DISK_FILE);
		if (file.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				String line;
				int i = 0;
				while ((line = br.readLine()) != null) {
					i++;
					LOG.info("OrderPayingCache initialize continue task order : {}", line);
					String[] orderDelayString = StringUtils.split(line, ORDERID_DELAY_SPLIT);
					schedule(orderDelayString[0], Integer.parseInt(orderDelayString[1]));
				}
				LOG.info("OrderPayingCache initialize the previously saved data, size : {}", i);
			} catch (Exception e) {
				LOG.info(e.getMessage(), e);
			} finally {
				if (br != null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				file.delete();
			}
		} else {
			LOG.info("Not exist order paying task cache");
		}
	}

	public void saveNotEndOfTask() {
		LOG.info("OrderPayingCache save cache to file");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(TO_DISK_FILE)));
			List<String> orderDelayTimes = getOrderPayingRemainTimes();
			int i = 0;
			for (String s : orderDelayTimes) {
				i++;
				bw.write(s);
				if (i < orderDelayTimes.size()) {
					bw.newLine();
				}
				bw.flush();
			}
			LOG.info("OrderPayingCache save cache to file, size : {}", i);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public boolean cancel(String orderNumber) {
		return cancel(orderNumber, true);
	}

	private boolean cancel(String orderNumber, boolean bool) {
		ScheduledFuture<?> sf = ORDER_FUTURE.remove(orderNumber);
		if (bool && sf != null) {
			return sf.cancel(true);
		}
		return true;
	}
	
	/**
	 * Tomcat停止时, 取消所有的任务
	 * @author Yang
	 * @version v1.0
	 * @date 2016年11月18日
	 */
	public void shutdownNow() {
		ScheduledFuture<?> sf = null;
		for (String orderNumber : ORDER_FUTURE.keySet()) {
			sf = ORDER_FUTURE.get(orderNumber);
			if (sf.isDone() || sf.isCancelled()) {
				continue;
			}
			sf.cancel(true);
		}
		SES.shutdownNow();
	}

}
