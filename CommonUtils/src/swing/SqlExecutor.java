package swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.apache.commons.io.FileUtils;

import util.DbUtil;

/**
 * sql执行器
 *
 * @author 陈庆勇
 * @date 2016年11月1日
 */
public class SqlExecutor extends JFrame {
	private static final long serialVersionUID = 8427662615727800893L;

	private Map<String, DbInfo> dbInfoMap = new HashMap<String, DbInfo>();
	private List<DbInfo> dbInfoList = new ArrayList<DbInfo>();
	private JTextArea in = new JTextArea();
	private JTextPane out = new JTextPane();

	public SqlExecutor() {
		super("SQL Executor");
		this.setSize(805, 680);
		this.setResizable(false);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 815)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-680)/3);

		this.init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * message
	 *
	 */
	private void init() {
		this.initDbConfig();

		JScrollPane scroll = new JScrollPane();
		JPanel jp = new JPanel();
		scroll.setViewportView(jp);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		int size = this.dbInfoMap.size();
		jp.setLayout(new GridLayout((int) Math.floor(size / 2), 2));
		for (DbInfo info : dbInfoList) {
			String dbName = info.getId();
			JCheckBox jCheckBox = new JCheckBox(dbName, true);
			jCheckBox.setSize(250, 30);
			jCheckBox.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jCheckBox = (JCheckBox) e.getSource();
					dbInfoMap.get(jCheckBox.getText()).setEnable(jCheckBox.isSelected());
				}
			});
			jp.add(jCheckBox);
		}
		scroll.setBounds(0, 0, 800, 100);

		JScrollPane scrollIn = new JScrollPane();
		scrollIn.setViewportView(in);
		in.setLineWrap(true);
		scrollIn.setBounds(0, 100, 400, 500);
		scrollIn.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JScrollPane scrollOut = new JScrollPane();
		out.setEditable(false);
		scrollOut.setViewportView(out);
		scrollOut.setBounds(400, 100, 400, 500);
		scrollOut.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JButton execute = new JButton("执行");
		execute.setBounds(275, 610, 100, 30);
		execute.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				String sqls = in.getText();
				if (sqls == null || sqls.trim().equals("")) {
					insertMessage(false, Color.RED, "请至少一条完整的SQL语句！！！！\n\n\n");
					return;
				}
				List<DbInfo> infos = new ArrayList<DbInfo>();
				for (DbInfo info : dbInfoList) {
					if (info.isEnable()) {
						infos.add(info);
					}
				}
				if (infos.size() == 0) {
					insertMessage(false, Color.RED, "请至少勾选一个数据库！！！！\n\n\n");
					return;
				}
				insertMessage(false, Color.BLACK, "开始执行！时间：【" + new java.util.Date().toLocaleString() + "】》》》》》》\n");
				for (String sql : sqls.split("(\\s+(GO|go|Go|gO)\\s*)|(\\s*;\\s*)")) {
					if (sql == null || "".equals(sql)) {
						continue;
					}
					insertMessage(true, Color.BLACK, "执行：" + sql + "\n");
					for (DbInfo info : infos) {
						String message = info.execute(sql);
						if(message.contains("成功")){
							insertMessage(false, Color.BLUE, "【" + info.getId() + "】执行结果：" + message + "\n");
						}else{
							insertMessage(false, Color.RED, "【" + info.getId() + "】执行结果：" + message + "\n");
						}
					}
				}
				insertMessage(false, Color.BLACK, "执行完成！时间：【" + new java.util.Date().toLocaleString()+"】《《《《《《\n\n\n");
			}
		});

		JButton clear = new JButton("清空");
		clear.setBounds(425, 610, 100, 30);
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				out.setText(null);
			}
		});
		
		this.setLayout(null);
		this.add(scroll);
		this.add(scrollIn);
		this.add(scrollOut);
		this.add(execute);
		this.add(clear);
	}

	private void initDbConfig() {
		Properties config = new Properties();
		try {
			config.load(FileUtils.openInputStream(new File(System.getProperty("user.dir") + File.separator + "conf"
					+ File.separator + "dbConfig.properties")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (Map.Entry<Object, Object> entry : config.entrySet()) {
			String id = entry.getKey().toString();
			String url = entry.getValue().toString();
			DbInfo info = new DbInfo(id, url);
			this.dbInfoMap.put(id, info);
			this.dbInfoList.add(info);
		}
		Collections.sort(this.dbInfoList);
	}
	
	private void insertMessage(boolean isBold, Color color, String message) {  
        StyledDocument doc = out.getStyledDocument();  
        SimpleAttributeSet attr = new SimpleAttributeSet();  
        StyleConstants.setBold(attr, isBold);  
        StyleConstants.setForeground(attr, color);  
        try {  
            doc.insertString(doc.getLength(), message, attr);  
        } catch (BadLocationException e) {
        	e.printStackTrace();
        }  
    }  

	/**
	 * message
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new SqlExecutor();
	}

}

enum DbType {
	ORACLE, MYSQL
}

class DbInfo implements Comparable<DbInfo>{
	private String id;
	private DbType dbType;
	private String url;
	private String user;
	private String pwd;
	private boolean enable = true;

	/**
	 * message
	 *
	 * @param dbType
	 * @param url
	 * @param user
	 * @param pwd
	 */
	public DbInfo(String id, String url) {
		if (url.indexOf(":oracle:") != -1) {
			this.dbType = DbType.ORACLE;
			int i = url.indexOf("/");
			this.user = url.substring(url.lastIndexOf(":", i) + 1, i);
			this.pwd = url.substring(i + 1, url.indexOf("@", i));
		} else {
			this.dbType = DbType.MYSQL;
		}
		this.id = id;
		this.url = url;
	}

	/**
	 * @return the dbType
	 */
	public DbType getDbType() {
		return this.dbType;
	}

	/**
	 * @param dbType
	 *            the dbType to set
	 */
	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return this.pwd;
	}

	/**
	 * @param pwd
	 *            the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return this.enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	/* 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DbInfo o) {
		return this.getId().compareTo(o.getId());
	}

	public String execute(String sql) {
		System.out.println(sql);
		String rec = null;
		try {
			Connection connection = DbUtil.getOracleConnection(this.url, this.user, this.pwd);
			int update = DbUtil.update(connection, sql);
			DbUtil.close(connection);
			rec = "执行成功！更新行数：" + update;
		} catch (Exception e) {
			rec = "执行异常：" + e.getMessage();
			e.printStackTrace();
		}
		return rec;
	}
}
