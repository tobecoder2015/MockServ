package com.wing.core.proxy;

import com.wing.config.Config;
import com.wing.core.domain.MockRequest;
import com.wing.core.domain.MockResponse;
import com.wing.core.filter.FilterChain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Slf4j
public class HttpProxyMainThread extends Thread {

	protected Socket csocket;// 与客户端连接的Socket

	public HttpProxyMainThread(Socket cs) {
		this.csocket = cs;
	}



	public void run() {
		String firstLine = ""; // http请求头第一行
		Socket ssocket = null;//与目标服务器连接的socket
		// cis为客户端输入流，sis为目标主机输入流
		InputStream cis = null, sis = null;
		// cos为客户端输出流，sos为目标主机输出流
		OutputStream cos = null, sos = null;
		try {
			csocket.setSoTimeout(Config.timeout);
			cis = csocket.getInputStream();
			cos = csocket.getOutputStream();
			StringBuilder sb=new StringBuilder();

			while (true) {
				int c = cis.read();
				if (c == -1)
					break; // -1为结尾标志
				if (c == '\r' || c == '\n')
					break;// 读入第一行数据,从中获取目标主机url
				sb.append((char) c);
			}
			firstLine = sb.toString();
			log.info("收到请求："+firstLine);




			if(StringUtils.isNotBlank(firstLine)) {

				MockRequest mockRequest = MockRequest.builder(firstLine);//将url封装成对象，完成一系列转换工作,并在getIP中实现了dns缓存
				firstLine = mockRequest.getFirstLine(firstLine);

				FilterChain filterChain=new FilterChain();
				MockResponse mockResponse=new MockResponse();
				if(filterChain.doFilter(mockRequest,mockResponse)) {
					cos.write(mockResponse.bytes());
					return;
				}




				log.debug("解析首行：" + firstLine);

				int retry = Config.connectRetries;
				while (retry-- != 0) {
					try {
						ssocket = new Socket(mockRequest.getIp(), Integer.parseInt(mockRequest.getPort())); // 尝试建立与目标主机的连接
						log.debug("successfully connect to (" + mockRequest.getIp() + ":" + mockRequest.getPort() + ")(host:" + mockRequest.getHost() + "),get resource(" + mockRequest.getQuery() + ")");
						break;
					} catch (Exception e) {
						log.error("fail connect to (" + mockRequest.getIp() + ":" + mockRequest.getPort() + ")(host:" + mockRequest.getHost() + ")");
					}
					// 等待
					Thread.sleep(Config.connectPause);
				}
				if (ssocket != null) {
					ssocket.setSoTimeout(Config.timeout);
					sis = ssocket.getInputStream();
					sos = ssocket.getOutputStream();
					sos.write(firstLine.getBytes()); // 将请求头写入
					pipe(cis, sis, sos, cos); // 建立通信管道
				}
			}
		} catch (Exception e) {
			log.error("处理请求失败：",e);
		} finally {
			try {
				csocket.close();
				cis.close();
				cos.close();
			} catch (Exception e1) {
				log.error("客户端socket关闭失败",e1);
			}
			try {
				if(ssocket!=null) {
					ssocket.close();
					sis.close();
					sos.close();
				}
			} catch (Exception e2) {
				log.error("客户端socket关闭失败",e2);
			}
		}
	}

	/**
	 * 为客户机与目标服务器建立通信管道
	 * @param cis 客户端输入流
	 * @param sis 目标主机输入流
	 * @param sos 目标主机输出流
	 * @param cos 客户端输出流
	 */
	public void pipe(InputStream cis, InputStream sis, OutputStream sos, OutputStream cos) {
		Client2ServerThread c2s = new Client2ServerThread(cis, sos);
		Server2ClientThread s2c = new Server2ClientThread(sis, cos);
		c2s.start();
		s2c.start();
		try {
			c2s.join();
			s2c.join();
		} catch (InterruptedException e1) {
			
		}
	}
}
