package com.draw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class GetPoint {

	public static void main(String[] args)throws Exception {
		BufferedImage image = ImageIO.read(new FileInputStream(new File("D:\\A\\1588.png"))); 
		int height=image.getHeight();
		int width=image.getWidth();
		
		int count=0;
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
//				if(i<width/4&&j<height/4){
//					continue;
//				}
				int color=image.getRGB(i,j);
				int[] rgb = new int[3];
				rgb[0] = (color & 0xff0000 ) >> 16 ; 
				rgb[1] = (color & 0xff00 ) >> 8 ; 
				rgb[2] = (color & 0xff ); 
				// 98B1D5
				if(rgb[0]>=150&&rgb[0]<170){
					image.setRGB(i, j,0xAAC5EE); //海颜色
					System.out.print("{"+i+","+j+"},");
				}else if(rgb[0]>=190&&rgb[0]<=244&&rgb[2]>200){
					image.setRGB(i, j,0xF4F4EE); //陆地颜色
					if(!checkExist(i, j)){
						System.out.print("{"+i+","+j+"},");
						count++;
					}
				}
			}
			System.out.println();
		}
		image.flush();
		ImageIO.write(image, "png",new File("D:\\A\\point.png"));
		System.out.println(count);
	}
	
	private static boolean  checkExist(int i,int j){
		for(int k=0;k<Points.points.length;k++){
			int x=Points.points[k][0];
			int y=Points.points[k][1];
			if(i==x&&y==j){
				return true;
			}
		}
		return false;
	}
	
	
}
