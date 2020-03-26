/**
 * 
 */
package swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * message
 *
 * @author 陈庆勇
 * @date 2016年11月3日
 */
@SuppressWarnings("serial")
public class Demo05 extends JFrame {

	/**
	 * message
	 *
	 */
	public Demo05() {
		super("Demo05");
		this.setSize(600, 400);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 600)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-400)/3);
		JButton myButton = new JButton();
		myButton.setBounds(280, 150, 17, 17);
		this.add(myButton);
		this.setLayout(null);
		myButton.addActionListener(event -> JOptionPane.showMessageDialog(null, "Click MyButton!"));
		myButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				myButton.setBackground(new Color(202,234,207));
				System.out.println("Exited");
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				myButton.setBackground(Color.BLUE);
				System.out.println("Entered");
			}
		});
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * message
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Demo05();

	}

}

@SuppressWarnings("serial")
class MyButton extends JButton {
	/**
	 * message
	 *
	 */
	public MyButton() {
		super("OK");
		this.setSize(17, 17);
		this.setBorder(null);
		this.setVisible(true);
	}

	/*
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// super.paint(g);
		g.setColor(new Color(202,234,207));
		// g.setFont(new Font("宋体", Font.BOLD, 26));
		g.fillRect(0, 0, 17, 17);
		// g.drawString("DrawString", 20, 80);
		try {
			BufferedImage img = ImageIO.read(this.getClass().getResource("/swing/ccs.png"));
			g.drawImage(img, 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
