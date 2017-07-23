package com.wing.core.proxy;

import com.wing.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 参考  http://www.java2s.com/Tutorial/Java/0490__Security/HTTPSServer.htm
 */
@Slf4j
public class HttpProxyStart implements Runnable{

	@Override
	public void run()  {
		int httpPort = Config.httpPort;

		ExecutorService executorServicePool= Executors.newFixedThreadPool(Config.httpPoolSize);

		ServerSocket httpServerSocket=null ;


		try {
			 httpServerSocket = new ServerSocket(httpPort);;

			log.info("The http proxy have start , port:" + httpPort);

			while (true) {
				try {
					Socket socketHttp = httpServerSocket.accept();

					//线程池处理
					executorServicePool.submit(new HttpProxyMainThread(socketHttp));

				} catch (Exception e) {
					log.info("Thread start fail");
				}
				Thread.sleep(Config.listen_pause);
			}
		} catch (Exception e1) {
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
