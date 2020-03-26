package util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

/**
 * org.apache.commons.io.FileUtils ��ʵ��Demo
 *
 * @author bravechen
 * @date 2015-11-26
 */
public class FileUtil {
	public static void main( String[] args ) {
		try {
			File cleanDir = new File( "clean/" );
			if ( !cleanDir.exists() || cleanDir.isFile() ) {
				cleanDir.mkdirs();
				System.out.println( cleanDir.getAbsolutePath() );
			}
			System.out.println( FileUtils.readFileToString( new File( "in.txt" ) ) );
			System.out.println( FileUtils.deleteQuietly( new File( "noFile.txt" ) ) );
			FileUtils.copyFileToDirectory( new File( "in.txt" ), cleanDir );
			System.out.println( FileUtils.readLines( new File( cleanDir, "in.txt" ) ) );
			FileUtils.copyURLToFile( new URL("http://www.baidu.com/"), new File("url2File.txt") );
			FileUtils.cleanDirectory( cleanDir );
			FileUtils.deleteQuietly( cleanDir );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
