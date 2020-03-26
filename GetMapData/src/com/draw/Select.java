package com.draw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class Select {

	private static ExecutorService threadPool=Executors.
			newFixedThreadPool(5);
	
	private static int count=0;
	
	private synchronized static void print(){
		count++;
		System.out.println("已处理："+count);
	}
	
	//D://apache-tomcat-6.0.26//webapps//tian//tiles//2
	//170 197 238  AAC5EE  98B1D5
	public static void main(String[] args)throws Exception {
		 File dir=new File("D://apache-tomcat-6.0.26//webapps//tian//tiles//10");
//		 File dir=new File("D://A//test");
		 visit(dir);
	}
	
	private static void visit(File dir)throws Exception{
		 for(File f:dir.listFiles()){
			 final File file=f;
			 if(f.isDirectory()){
				 visit(f);
			 }else{
				 threadPool.execute(new Runnable(){

					@Override
					public void run() {
						 try {
							process(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				 }); 
			 }
		 }
	}
	
	// 水印颜色 D6D5D0
	private static void process(File file)throws Exception{
		
		BufferedImage image = ImageIO.read(new FileInputStream(file)); 
		int height=image.getHeight();
		int width=image.getWidth();
		
		int count=0;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				int color=image.getRGB(i,j);
				int[] rgb = new int[3];
				rgb[0] = (color & 0xff0000 ) >> 16 ; 
				rgb[1] = (color & 0xff00 ) >> 8 ; 
				rgb[2] = (color & 0xff ); 
				// 98B1D5  186 146 240    209 208 196
				if(rgb[0]>=150&&rgb[0]<170){
//					image.setRGB(i, j,0xAAC5EE); //海颜色
					count++;
				}else if(rgb[0]>=190&&rgb[0]<=244&&rgb[1]>200&&rgb[2]>190&&rgb[2]<220
						&&checkLine12(color)){
//					image.setRGB(i, j,0xF4F4EE); //陆地颜色
					count++;
				}
			}
		}
//		image.flush();
		print();
		if(count>500){
			ImageIO.write(image, "png",new File("D:\\A\\target\\"+file.getName()));
		}
	}
	
	/**
	 * 检验12级下灰色交通线图
	 * @return
	 */
	private static boolean checkLine12(int color){
		int c1=0xffDFDFD7;
		int c2=0xffE4E3DC;
		int c3=0xffE8E8E1;
		int c4=0xffE1E1D9;
		int c5=0xffE6E6DE;
		int c6=0xffF5F4ED;
		int c7=0xffE5E5DD;
		int c8=0xffF2F2EC;
		//int c9=0xffDEDED6;
		int c10=0xffEDECE5;
		if(color==c1||color==c2||color==c3||color==c4||color==c5
				||color==c6||color==c7||color==c8||color==c8||color==c10){
			return false;
		}
		return true;
	}
}
