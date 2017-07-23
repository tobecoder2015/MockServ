package com.wing.core.proxy;

import com.wing.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 参考  http://www.java2s.com/Tutorial/Java/0490__Security/HTTPSServer.htm
 */
@Slf4j
public class HttpsProxyStart implements Runnable{

	@Override
	public void run()  {
		int httpsPort = Config.httpsPort;

		System.setProperty("javax.net.ssl.trustStore", "clienttrust");
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

		ExecutorService executorServicePool= Executors.newFixedThreadPool(Config.httpsPoolSize);

		ServerSocket httpsServerSocket=null ;


		try {
			 httpsServerSocket = ssf.createServerSocket(httpsPort);

			log.info("The https proxy have start ,  port:"+httpsPort);

			while (true) {
				try {
					Socket socketHttps = httpsServerSocket.accept();

					//线程池处理
					executorServicePool.submit(new HttpsProxyMainThread(socketHttps));
				} catch (Exception e) {
					log.info("Thread start fail");
				}
				Thread.sleep(Config.listen_pause);
			}
		} catch (Exception e1) {
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
