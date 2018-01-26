package com.appserver.quartz;

import com.commons.util.FileUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用来每天定时备份数据库
 * 
 * @author wenwen 2015-5-30 下午05:51:43
 */
public class BackupSql extends QuartzJobBean {

	private final static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Runtime run = Runtime.getRuntime();
		Process process = null;
		BufferedReader br = null;
		try {
			String time = df.format(new Date());
			String path = "/carlifebackup/" + time + ".sql";
			String mysql = "/usr/local/mysql/bin/mysqldump -u root -pHNcar666666 carlife>" + path;
			process = run.exec(new String[] { "sh", "-c", mysql });

			br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			String file = path;
			String zipFile1 = "/carlifebackup/" + time + ".zip";
			FileUtil.zipSingleFile(file, zipFile1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
