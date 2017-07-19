package com.wing;

import com.wing.core.proxy.HttpProxyMainThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class ProxyStart implements CommandLineRunner{

	@Override
	public void run(String... strings) throws Exception {
		int port = 9111;
		ServerSocket serverSocket = null;
		ExecutorService executorServicePool= Executors.newFixedThreadPool(50);

		try {
			serverSocket = new ServerSocket(port);
			log.info("The proxy have start on port:" + port + "\n");
			while (true) {
				Socket socket = null;
				try {
					socket = serverSocket.accept();
					//线程池处理
					executorServicePool.submit(new HttpProxyMainThread(socket));
				} catch (Exception e) {
					log.info("Thread start fail");
				}
			}
		} catch (IOException e1) {
			log.error("proxyd start fail",e1);
		}finally{
			try {
				serverSocket.close();
			} catch (IOException e) {
				log.error("proxyd close fail",e);
			}
		}
	}
}
