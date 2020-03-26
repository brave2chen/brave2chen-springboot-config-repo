package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

/**
 * org.apache.commons.io.IOUtils 腔妗蚚Demo
 *
 * @author bravechen
 * @date 2015-11-26
 */
public class IOUtil {
	public static void main( String[] args ) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream( "in.txt" );
			out = new FileOutputStream( "out.txt" );
		} catch ( FileNotFoundException e ) {
			e.printStackTrace();
		}
		try {
			IOUtils.copy( in, out );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
		IOUtils.closeQuietly( in );

		try {
			System.out.println( IOUtils.toString( new FileInputStream( "in.txt" ) ) );
			System.out.println( IOUtils.readLines( new FileInputStream( "in.txt" ) ) );
			
			IOUtils.write( "\nIOUtils.write something!", out );
			System.out.println( IOUtils.toString( new FileInputStream( "out.txt" ) ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		IOUtils.closeQuietly( in );
		IOUtils.closeQuietly( out );
	}
}
