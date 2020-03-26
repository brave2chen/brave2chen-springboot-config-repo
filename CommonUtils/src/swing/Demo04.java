/**
 * 
 */
package swing;

import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * message
 *
 * @author 陈庆勇
 * @date 2016年11月3日
 */
public class Demo04 extends JFrame {
	/** message */
	private static final long serialVersionUID = -7814305813236096684L;
	private JTabbedPane top = new JTabbedPane(JTabbedPane.TOP);
	private JPanel pan = new JPanel();
	private JButton but = new JButton("OK");
	private JButton cancle = new JButton("Cancel");

	private DefaultComboBoxModel<String> dcm = new DefaultComboBoxModel<>(new String[] { "Default" });
	private JComboBox<String> jcom = new JComboBox<String>(dcm);
	private JTextField jtf = new JTextField(25);

	/**
	 * message
	 *
	 */
	public Demo04() {
		super("Demo04");
		this.setSize(600, 400);
		this.setLocation(300, 200);

		// 无效
		this.setBackground(Color.GREEN);

		// 有效
		this.getContentPane().setBackground(Color.YELLOW);

		pan.add(jcom);
		pan.add(jtf);
		pan.add(but);
		top.add("Tab1", pan);
		top.add("Tab2", cancle);
		this.add(top);

		jtf.setText("Add to comboBox!");
		but.addActionListener(event -> dcm.addElement(jtf.getText()));

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
		new Demo04();
	}

}
