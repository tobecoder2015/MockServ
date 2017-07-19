package com.wing;

import com.wing.core.proxy.HttpProxyMainThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
public class ProxyStart implements CommandLineRunner{

	@Override
	public void run(String... strings) throws Exception {
			int port = 9111;
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(port);
				log.info("The proxy have start on port:" + port + "\n");
				while (true) {
					Socket socket = null;
					try {
						socket = serverSocket.accept();
						new HttpProxyMainThread(socket).start();//有一个请求就启动一个线程
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
