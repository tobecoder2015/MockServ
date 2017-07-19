package com.wing;

import com.wing.core.proxy.HttpProxyMainThread;
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
@Component
@Order(value = 2)
public class HttpProxyStart implements CommandLineRunner{

	@Override
	public void run(String... strings) throws Exception {
		int httpPort = 9111;

		ExecutorService executorServicePool= Executors.newFixedThreadPool(50);

		ServerSocket httpServerSocket=null ;


		try {
			 httpServerSocket = new ServerSocket(httpPort);;

			log.info("The proxy have start ,http port:" + httpPort);

			while (true) {
				try {
					Socket socketHttp = httpServerSocket.accept();

					//线程池处理
					executorServicePool.submit(new HttpProxyMainThread(socketHttp));
				} catch (Exception e) {
					log.info("Thread start fail");
				}
			}
		} catch (IOException e1) {
			log.error("proxyd start fail",e1);
		}finally{
			try {
				if(httpServerSocket!=null)
					httpServerSocket.close();

			} catch (IOException e) {
				log.error("proxyd close fail",e);
			}
		}
	}
}
