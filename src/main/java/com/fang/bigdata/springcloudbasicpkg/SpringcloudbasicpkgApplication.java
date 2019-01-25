package com.fang.bigdata.springcloudbasicpkg;

import com.fang.bigdata.springcloudbasicpkg.bean.DbPropertiesMap;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class SpringcloudbasicpkgApplication {

	@Value("${elasticsearch.cluster_name}")
	private String CLUSTER_NAME;
	//ES集群中某个节点
	@Value("${elasticsearch.hostname}")
	private String HOSTNAME;
	//连接端口号
	@Value("${elasticsearch.tcp_port}")
	private String TCP_PORT;
	@Value("${datasource.dbpassword}")
	private String DB_PASSWORD;

	/**
	 * 生成一个 TransportClient 用于es查询
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	@Bean
	public TransportClient getClient() throws UnknownHostException {
		String[] hosts = HOSTNAME.split(",");
		PreBuiltTransportClient client = new PreBuiltTransportClient(Settings.builder().put("cluster.name", CLUSTER_NAME).build());
		for (String host : hosts) {
			client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), new Integer(TCP_PORT)));
		}
		return client;
	}

	@Bean
	public DbPropertiesMap getPropertiesMap() {
		try {
			File file = new File(DB_PASSWORD);
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			Map<String, String> m = new HashMap();
			String text = null;
			while ((text = bufferedReader.readLine()) != null) {
				String[] s = text.split("=", -1);
				if (s.length == 1)
					continue;
				String key = s[0];
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < s.length; i++) {
					if (s.length - i > 1)
						sb.append(s[i] + "=");
					else
						sb.append(s[i]);
				}
				m.put(key, sb.toString());
			}
			DbPropertiesMap dbPropertiesMap = new DbPropertiesMap();
			dbPropertiesMap.setM(m);

			return dbPropertiesMap;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("请创建数据库配置文件");
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringcloudbasicpkgApplication.class, args);
	}

}