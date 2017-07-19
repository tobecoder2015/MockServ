package com.wing.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class Server2ClientThread extends Thread{
	private InputStream sis;
    private OutputStream cos;

    public Server2ClientThread(InputStream sis, OutputStream cos) {
        this.sis = sis;
        this.cos = cos;
    }

    public void run() {
    	int length;
    	byte bytes[] = new byte[1024];
		while(true){
			try {
				if ((length = sis.read(bytes)) > 0) {
					cos.write(bytes, 0, length);
					cos.flush();
				} else if (length < 0)
					break;
			} catch (Exception e) {
//				log.error("读取目标主机失败：",e);
			}
		}
    }
}
