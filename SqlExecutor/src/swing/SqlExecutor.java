package swing;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import org.apache.commons.lang3.time.DateFormatUtils;

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
	final JButton execute = new JButton("执行");

	public SqlExecutor() {
		super("SQL Executor");
		this.setSize(805, 680);
		this.setResizable(false);
		this.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - 815) / 2,
				(Toolkit.getDefaultToolkit().getScreenSize().height - 680) / 3);

		this.init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for (DbInfo info : dbInfoList) {
					info.closeConnection();
				}
			}
		});
		this.setVisible(true);

		this.initDbConnection();

		this.in.requestFocus();
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

		int size = this.dbInfoList.size();
		jp.setLayout(new GridLayout((int) Math.ceil(size / 3D), 3));
		for (DbInfo info : dbInfoList) {
			String dbName = info.getId();
			JCheckBox jCheckBox = new JCheckBox(dbName);
			jCheckBox.setSize(250, 30);
			jCheckBox.setEnabled(false);
			jCheckBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JCheckBox jCheckBox = (JCheckBox) e.getSource();
					if (jCheckBox.getForeground() == Color.RED) {
						jCheckBox.setSelected(false);
					}
					dbInfoMap.get(jCheckBox.getText()).setEnable(jCheckBox.isSelected());
				}
			});
			jp.add(jCheckBox);
			info.setCheckBox(jCheckBox);
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

		execute.setBounds(275, 610, 100, 30);
		execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sqls = in.getText();
				int start = in.getSelectionStart();
				int end = in.getSelectionEnd();
				if (start != end) {
					sqls = sqls.substring(in.getSelectionStart(), in.getSelectionEnd());
				}
				if (sqls == null || sqls.trim().equals("")) {
					insertMessage(false, Color.RED, "请输入至少一条完整的SQL语句！！！！\n\n\n");
					return;
				}
				if (start != end) {
					in.requestFocus();
					in.select(start, end);
				}
				executeSqls(sqls);
			}
		});
		in.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_E && execute.isEnabled()) {
					String sqls = in.getText();
					int start = in.getSelectionStart();
					int end = in.getSelectionEnd();
					if (start != end) {
						sqls = sqls.substring(in.getSelectionStart(), in.getSelectionEnd());
					}
					if (sqls == null || sqls.trim().equals("")) {
						insertMessage(false, Color.RED, "请输入至少一条完整的SQL语句！！！！\n\n\n");
						return;
					}
					if (start != end) {
						in.requestFocus();
						in.select(start, end);
					}
					executeSqls(sqls);
				}
				if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_L) {
					out.setText(null);
				}
				if (e.getModifiers() == KeyEvent.CTRL_MASK && e.getKeyCode() == KeyEvent.VK_R && in.isEditable()) {
					new Thread() {
						public void run() {
							insertMessage(false, Color.RED, "获取断开的数据连接开始，时间：【"
									+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "】！！！！\n");
							initDbConnection();
							insertMessage(false, Color.RED, "获取断开的数据连接结束，时间：【"
									+ DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "】！！！！\n\n\n");
						}
					}.start();
				}
			}
		});

		JButton clear = new JButton("清空");
		clear.setBounds(425, 610, 100, 30);
		clear.addActionListener(new ActionListener() {
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
		FileInputStream fileInputStream = null;
		try {
			File file = new File(System.getProperty("user.dir") + File.separator + "conf" + File.separator + "dbConfig.properties");
			fileInputStream = new FileInputStream(file);
			config.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
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

	private void initDbConnection() {
		this.in.setEditable(false);
		this.execute.setEnabled(false);
		for (DbInfo info : this.dbInfoList) {
			String result = info.openConnection();
			info.getCheckBox().setForeground(result == null ? Color.BLUE : Color.RED);
			info.getCheckBox().setSelected(result == null);
			info.getCheckBox().setEnabled(true);
			info.getCheckBox().setToolTipText(result);
			info.setEnable(info.getCheckBox().isSelected());
			info.closeConnection();
		}
		this.execute.setEnabled(true);
		this.in.setEditable(true);
	}

	synchronized private void insertMessage(boolean isBold, Color color, String message) {
		StyledDocument doc = out.getStyledDocument();
		SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setBold(attr, isBold);
		StyleConstants.setForeground(attr, color);
		try {
			doc.insertString(doc.getLength(), message, attr);
			out.selectAll();
			// in.requestFocus();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * message
	 *
	 * @param sqls
	 */
	private void executeSqls(final String sqls) {
		final List<DbInfo> infos = new ArrayList<DbInfo>();
		for (DbInfo info : dbInfoList) {
			if (info.isEnable()) {
				infos.add(info);
			}
		}
		if (infos.size() == 0) {
			insertMessage(false, Color.RED, "请至少勾选一个数据库！！！！\n\n\n");
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				execute.setEnabled(false);
				Date start = new Date();
				insertMessage(false, Color.BLACK,
						"开始执行！时间：【" + DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss.SSS") + "】》》》》》》\n");
				for (String sql : sqls.split("(\\s+(GO|go|Go|gO)\\s*)|(\\s*;\\s*)")) {
					sql = sql.trim();
					if (sql == null || "".equals(sql)) {
						continue;
					}
					ExecutorService service = Executors.newFixedThreadPool(infos.size());
					List<ExecuteSqlTask> tasks = new ArrayList<ExecuteSqlTask>();
					insertMessage(true, Color.BLACK, "执行：" + sql + "\n");
					for (DbInfo info : infos) {
						tasks.add(new ExecuteSqlTask(info, sql));
					}
					try {
						service.invokeAll(tasks);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						service.shutdown();
					}
				}
				Date end = new Date();
				insertMessage(false, Color.BLACK, "执行完成！时间：【" + DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss.SSS")
						+ "】耗时：" + ((end.getTime() - start.getTime()) / 1000D) + "s\t《《《《《《\n\n\n");
				for(DbInfo info : infos){
					info.closeConnection();
				}
				execute.setEnabled(true);
			}
		}).start();
		;
	}

	class ExecuteSqlTask implements Callable<String> {
		private DbInfo info;
		private String sql;

		/**
		 * message
		 *
		 * @param info
		 * @param sql
		 */
		public ExecuteSqlTask(DbInfo info, String sql) {
			super();
			this.info = info;
			this.sql = sql;
		}

		/*
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public String call() throws Exception {
			String message = info.execute(sql);
			if (message.contains("成功")) {
				insertMessage(false, Color.BLUE, "【" + info.getId() + "】" + message + "\n");
			} else {
				insertMessage(false, Color.RED, "【" + info.getId() + "】" + message + "\n");
			}
			return message;
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

class DbInfo implements Comparable<DbInfo> {
	private String id;
	private DbType dbType;
	private String url;
	private String user;
	private String pwd;
	private boolean enable = false;
	private Connection connection;
	private JCheckBox checkBox;

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
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @return the pwd
	 */
	public String getPwd() {
		return this.pwd;
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

	/**
	 * @return the checkBox
	 */
	public JCheckBox getCheckBox() {
		return this.checkBox;
	}

	/**
	 * @param checkBox
	 *            the checkBox to set
	 */
	public void setCheckBox(JCheckBox checkBox) {
		this.checkBox = checkBox;
	}

	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(DbInfo o) {
		return this.getId().compareTo(o.getId());
	}

	public String execute(String sql) {
		long start = System.currentTimeMillis();
		String rec = this.openConnection();
		if (rec != null) {
			this.getCheckBox().setForeground(Color.RED);
			this.getCheckBox().setToolTipText(rec);
			this.getCheckBox().setSelected(false);
			this.setEnable(false);
			return rec;
		}
		try {
			int update = DbUtil.update(connection, sql);
			long end = System.currentTimeMillis();
			rec = "耗时:" + (end - start) + "ms, 执行成功！更新行数：" + update;
		} catch (Exception e) {
			long end = System.currentTimeMillis();
			rec = "耗时:" + (end - start) + "ms, 执行异常！异常描述：" + e.getMessage();
			e.printStackTrace();
		}
		return rec;
	}

	public String openConnection() {
		String ret = null;
		try {
			if (connection == null ) {
				connection = DbUtil.getOracleConnection(this.url, this.user, this.pwd);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ret = "异常：" + e.getMessage();
		}
		return ret;
	}

	public void closeConnection() {
		if (connection != null) {
			DbUtil.closeQuietly(connection);
			connection = null;
		}
	}
}
