/**
 * 
 */
package swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * message
 *
 * @author 陈庆勇
 * @date 2016年11月3日
 */
public class Demo06 {
	/**
	 * message
	 *
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		int width = 400;
		int height = 100;
		int size = 50;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics graphics = img.getGraphics();
		Font font = new Font("宋体", Font.BOLD, size);
		graphics.setFont(font);
		graphics.setColor(new Color(225, 230, 246));
		graphics.fillRect(0, 0, width, height);
		
		Random random = new Random();
		
		int length = 6;
		for (int i = 0; i < length; i++) {
			int x = (width-size*length)/(length+1)*(i + 1) + size * i;
			int y = random.nextInt(height-size) + size;
			int a = random.nextInt(50)+200;
			int b = random.nextInt(150)+100;
			int c = random.nextInt(150)+100;
			String s = String.valueOf((char)(random.nextInt(26) + 'A'));
			graphics.setColor(new Color(a, b, c));
			graphics.drawString(s, x, y);
		}
		int count = 60;
		for (int i = 0; i < count; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			int a = random.nextInt(256);
			int b = random.nextInt(256);
			int c = random.nextInt(256);
			graphics.setColor(new Color(a, b, c));
			graphics.drawLine(x1, y1, x2, y2);
		}
		
		ImageIO.write(img, "jpg", new File("test.jpg"));
	}

	
	
}
