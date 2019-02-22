package org.wc.socket.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServer {
	private final static Logger logger = (Logger) LoggerFactory.getLogger(SocketServer.class);
	
	public static void main(String[] args) {
		if (checkArgs(args)) {
			try {
				ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
				logger.info("服务已启动，等待客户端连接..");
				
				while (true) {
					Socket socket = serverSocket.accept();
					logger.info("收到来自客户端[" + socket.getInetAddress().getHostAddress() + "]的连接");			
					OutputStream outputStream = socket.getOutputStream();
					PrintWriter printWriter = new PrintWriter(outputStream);
					printWriter.print(socket.getInetAddress().getHostAddress());
					printWriter.flush();
					socket.shutdownOutput();
					logger.info("已将IP地址送回客户端");
					
					printWriter.close();
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}		
	}
	
	private static boolean checkArgs(String[] args) {
		if (args.length != 1) {
			System.out.println("只接受一个参数，且为服务侦听端口号");
			return false;
		}	
		try {
			int port = Integer.parseInt(args[0]);
			//logger.info("第一个参数为:" + port);		
			if ((port < 1) || (port > 65536)) {
				logger.error("端口非法");
				System.out.println("端口参数非法，请校验");
			}
			return true;
		} catch (NumberFormatException e) {
			System.out.println("请输入正确的端口号!");
			return false;
		} catch (Exception e) {
			System.out.println("服务未启动，请查看日志");
			e.printStackTrace();
			return false;
		}
	}
}
