package com.wing.core.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Data
@Slf4j
public class MockRequest {
	private String scheme;
	private String host;
	private String ip;
	private String query;
	private String port;
	private String url;
	private Map<String,String> headers;
	private String body;


	public static String getIp(String host) {
		java.security.Security.setProperty("networkaddress.cache.ttl", "30");
		try {
			return InetAddress.getByName(host).getHostAddress();
		} catch (UnknownHostException e) {
			log.error("获取ip地址失败："+host);
			return  null;
		}
	}


	/**
	 *  获取处理后的首行
	 * @param firstLine
	 * @return
	 */
	public String getFirstLine(String firstLine){

		if(this.scheme.equalsIgnoreCase("http")){
			return firstLine.replace(url,"");
		}else{
			return firstLine;
		}
	}

	@Override
	public String toString() {
		return "scheme:" + this.getScheme() + "\nhost:" + this.getHost()
				+ "\nport:" + this.getPort() + "\nip:"+this.getIp()+"\nResource:"+this.getQuery();
	}

	/**
	 * 根据首行构建URL
	 * @param firstLine
	 * @return
	 */
	public static MockRequest builder(String firstLine) {
		String scheme = "http";
		String host = "";
		String port = "80";
		int index=0;
		String url="";
		String query="";

		MockRequest newUrl=new MockRequest();

		try {
			String[] tokens = firstLine.split(" ");
			if (firstLine.startsWith("CONNECT") && firstLine.contains("443")) {
				scheme = "https";
				url = tokens[1];
			} else {
				for (; index < tokens.length; index++) {
					if (tokens[index].startsWith("http://")) {
						url = tokens[index];
						break;
					}
				}
			}
			if (scheme.equals("http")) {
				index = url.indexOf("//");
				host = url.substring(index + 2);
				index = host.indexOf('/');
				if (index != -1) {
					query = host.substring(index);
					host = host.substring(0, index);
				}
				index = host.indexOf(':');
				if (index != -1) {
					port = host.substring(index + 1);
					host = host.substring(0, index);
					url = "http://" + host + ":" + port;
				} else {
					url = "http://" + host;
				}

			} else {
				host = url.substring(0, url.indexOf(":"));
				port = url.substring(url.indexOf(":") + 1);
			}
		}catch (Exception e){
			log.info("url解析失败："+firstLine);
			throw e;
		}
		newUrl.setHost(host);
		newUrl.setIp(getIp(host));
		newUrl.setPort(port);
		newUrl.setQuery(query);
		newUrl.setScheme(scheme);
		newUrl.setUrl(url);
        return newUrl;
	}
}
