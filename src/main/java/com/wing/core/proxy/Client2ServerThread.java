package com.wing.core.proxy;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
public class Client2ServerThread extends Thread{
	private InputStream cis;
    private OutputStream sos;

    public Client2ServerThread(InputStream cis, OutputStream sos) {
        this.cis = cis;
        this.sos = sos;
    }

    public void run() {
    	int length;
    	byte bytes[] = new byte[1024];
		while(true){
			try {
				if ((length = cis.read(bytes)) > 0) {
					sos.write(bytes, 0, length);//将http请求头写到目标主机
					sos.flush();
				} else if (length < 0)
					break;
			} catch (Exception e) {
//				log.error("将http请求头写到目标主机：",e);
			}
		}
    }
}
