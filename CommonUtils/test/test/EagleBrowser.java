package test;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

@SuppressWarnings( "serial" )
public class EagleBrowser extends JPanel {

	private JPanel webBrowserPanel;

	private JWebBrowser webBrowser;

	public EagleBrowser( String url ) {
		super( new BorderLayout() );
		webBrowserPanel = new JPanel( new BorderLayout() );
		webBrowser = new JWebBrowser();
		webBrowser.navigate( url );
		webBrowser.setButtonBarVisible( false );
		webBrowser.setMenuBarVisible( false );
		webBrowser.setBarsVisible( false );
		webBrowser.setStatusBarVisible( false );
		webBrowserPanel.add( webBrowser, BorderLayout.CENTER );
		add( webBrowserPanel, BorderLayout.CENTER );
	}

	public static void main( String[] args ) {
		final String url = "http://www.baidu.com/";
		final String title = "集团传输网管验收测试机";
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();

		SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				JFrame frame = new JFrame( title );
				frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				frame.getContentPane().add( new EagleBrowser( url ), BorderLayout.CENTER );
				frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
				frame.setLocationByPlatform( true );
				//frame.setUndecorated(true);
				frame.setVisible( true );
			}
		} );
		NativeInterface.runEventPump();
	}

}
