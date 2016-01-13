package edu.must.tos.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class TimerTest {

	public static void main(String[] args) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp time1 = new Timestamp(new Date().getTime());
		Timestamp time2 = new Timestamp(sf.parse("2012-12-05").getTime());
		System.out.println(time1.compareTo(time2));
		int k = 0xcafe;
		System.out.println(k);
		
		String chiefId = "132;465;798;";
		String other = chiefId.substring(chiefId.indexOf(";"), chiefId.length());
		System.out.println(other);
		/*
		Timer timer = new Timer();
		timer.schedule(new MyTask(), 1000, 2000);// 在1秒后执行此任务,每次间隔2秒

		while (true) {// 这个是用来停止此任务的,否则就一直循环执行此任务了
			try {
				int ch = System.in.read();
				if (ch - 'c' == 0) {
					timer.cancel();// 使用这个方法退出任务
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}

	static class MyTask extends java.util.TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("我是来测试的。。我两秒出来一次");
		}
	}
	
}