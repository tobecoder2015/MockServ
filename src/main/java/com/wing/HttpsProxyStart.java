package com.wing;

import com.wing.core.proxy.HttpProxyMainThread;
import com.wing.core.proxy.HttpsProxyMainThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 参考  http://www.java2s.com/Tutorial/Java/0490__Security/HTTPSServer.htm
 */
@Slf4j
@Component
@Order(value = 1)
public class HttpsProxyStart implements CommandLineRunner{

	@Override
	public void run(String... strings) throws Exception {
		int httpsPort = 443;

		System.setProperty("javax.net.ssl.trustStore", "clienttrust");
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		ExecutorService executorServicePool= Executors.newFixedThreadPool(50);

		ServerSocket httpsServerSocket=null ;


		try {
			 httpsServerSocket = ssf.createServerSocket(httpsPort);

			log.info("The proxy have start , https port:"+httpsPort);

			while (true) {
				try {
					Socket socketHttps = httpsServerSocket.accept();

					//线程池处理
					executorServicePool.submit(new HttpsProxyMainThread(socketHttps));
				} catch (Exception e) {
					log.info("Thread start fail");
				}
			}
		} catch (IOException e1) {
			log.error("proxyd start fail",e1);
		}finally{
			try {
				if(httpsServerSocket!=null)
			     	httpsServerSocket.close();

			} catch (IOException e) {
				log.error("proxyd close fail",e);
			}
		}
	}
}
