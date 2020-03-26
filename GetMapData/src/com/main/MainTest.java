package com.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.model.Lnglat;
import com.model.Tile;
import com.util.ConfigUtil;
import com.util.CoordinateUtil;
import com.util.DownloadUtil;

public class MainTest {

	static ExecutorService pool;
	static String downloadDir;
	static int[] zoom;
	static Lnglat leftTopLnglat;
	static Lnglat rightBottomLnglat;
	static int roundCount;
	static int totalSize;
	static int currentIndex = 0;
	static int failedCount = 0;
	static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	static ReentrantReadWriteLock lockFailCount = new ReentrantReadWriteLock();
	static String mapUrl = "";
	static int mapType = 1;
	static int mapUrlEnable = 0;

	public static int addCurrentIndex() {
		synchronized ( lock.writeLock() ) {
			try {
				lock.writeLock().lock();
				currentIndex++;
			} finally {
				lock.writeLock().unlock();
			}
		}
		return currentIndex;
	}

	public static void addFailedCount() {
		synchronized ( lockFailCount.writeLock() ) {
			try {
				lockFailCount.writeLock().lock();
				failedCount++;
			} finally {
				lockFailCount.writeLock().unlock();
			}
		}
	}

	public static int getFailedCount() {
		int count = 0;
		synchronized ( lockFailCount.readLock() ) {
			try {
				lockFailCount.readLock().lock();
				count = failedCount;
			} finally {
				lockFailCount.readLock().unlock();
			}
		}
		return count;
	}

	public static void main( String[] args ) {
		init();
		printCalculatSize( zoom, leftTopLnglat, rightBottomLnglat );
		startDownload( zoom, leftTopLnglat, rightBottomLnglat );
	}

	private static void init() {
		try {
			boolean isProxy = false;
			isProxy = Boolean.valueOf( ConfigUtil.get( "isProxy" ) );
			if ( isProxy ) {
				DownloadUtil.setProxy( isProxy );
				DownloadUtil.setProxyAddress( ConfigUtil.get( "proxyAddress" ) );
				try {
					DownloadUtil.setProxyPort( Integer.parseInt( ConfigUtil.get( "proxyPort" ) ) );
				} catch ( Exception e ) {}
			}
		} catch ( Exception e ) {}
		downloadDir = ConfigUtil.get( "downloadDir" );
		mapUrl = ConfigUtil.get( "mapUrl" );
		mapType = Integer.valueOf( ConfigUtil.get( "mapType" ) );
		mapUrlEnable = Integer.valueOf( ConfigUtil.get( "mapUrlEnable" ) );
		pool = Executors.newFixedThreadPool( Integer.parseInt( ConfigUtil.get( "threadCount" ) ) );
		roundCount = Integer.parseInt( ConfigUtil.get( "roundCount" ).trim() );
		String[] tmpZoom = ConfigUtil.get( "zoom" ).split( "," );
		zoom = new int[tmpZoom.length];
		for ( int i = 0, len = zoom.length; i < len; i++ ) {
			zoom[i] = Integer.parseInt( tmpZoom[i].trim() );
		}

		String[] tmpLngLat = ConfigUtil.get( "leftTopLnglat" ).split( "," );
		leftTopLnglat = new Lnglat( Double.valueOf( tmpLngLat[0].trim() ), Double.valueOf( tmpLngLat[1].trim() ) );
		tmpLngLat = ConfigUtil.get( "rightBottomLnglat" ).split( "," );
		rightBottomLnglat = new Lnglat( Double.valueOf( tmpLngLat[0].trim() ), Double.valueOf( tmpLngLat[1].trim() ) );;
	}

	private static void printCalculatSize( int[] zoom, Lnglat leftTopLnglat, Lnglat rightBottomLnglat ) {
		int size = calculateDownloadSize( zoom, leftTopLnglat, rightBottomLnglat );
		System.out.println( "文件总数:" + size );
		System.out.println( "文件大小:" + size * 22 + "k, " + size * 22 / 1024 + "M, " + size * 22 / 1024 / 1024 + "G" );
	}

	private static int calculateDownloadSize( int[] zoom, Lnglat leftTopLnglat, Lnglat rightBottomLnglat ) {
		int size = 0;
		for ( int i = 0, len = zoom.length; i < len; i++ ) {
			Tile leftTopTile = CoordinateUtil.lnglatToTile( zoom[i], leftTopLnglat );
			Tile rightBottomTile = CoordinateUtil.lnglatToTile( zoom[i], rightBottomLnglat );

			size += ( rightBottomTile.getX() - leftTopTile.getX() + 1 ) * ( rightBottomTile.getY() - leftTopTile.getY() + 1 );
		}
		return size;
	}

	private static void startDownload( int[] zoom, Lnglat leftTopLnglat, Lnglat rightBottomLnglat ) {
		totalSize = calculateDownloadSize( zoom, leftTopLnglat, rightBottomLnglat );

		List<Tile> tmpTileList = new ArrayList<Tile>();
		int tmpTileListSize = 0;
		for ( int i = 0, len = zoom.length; i < len; i++ ) {
			Tile leftTopTile = CoordinateUtil.lnglatToTile( zoom[i], leftTopLnglat );
			Tile rightBottomTile = CoordinateUtil.lnglatToTile( zoom[i], rightBottomLnglat );

			for ( int x = leftTopTile.getX(); x <= rightBottomTile.getX(); x++ ) {
				for ( int y = leftTopTile.getY(); y <= rightBottomTile.getY(); y++ ) {
					tmpTileList.add( new Tile( x, y, zoom[i] ) );
					tmpTileListSize++;
					if ( tmpTileListSize >= roundCount ) {
						startDownloadThread( tmpTileList );
						tmpTileListSize = 0;
						tmpTileList = new ArrayList<Tile>();
					}
				}
			}
		}
		if ( tmpTileListSize != 0 ) {
			tmpTileListSize = 0;
			startDownloadThread( tmpTileList );
		}
	}

	private static void startDownloadThread( List<Tile> tmpTileList ) {
		final Tile[] threadTaskTiles = tmpTileList.toArray( new Tile[0] );
		pool.execute( new Runnable() {
			@Override
			public void run() {
				for ( int i = 0, len = threadTaskTiles.length; i < len; i++ ) {
					try {
						downloadXYZ( threadTaskTiles[i] );
						System.out.println( "当前文件 " + addCurrentIndex() + " 文件总数 " + totalSize + "下载失败 " + MainTest.getFailedCount() + "张" );
						if(currentIndex == totalSize){
							pool.shutdown();
						}
					} catch ( Exception e ) {
						i--;
						System.out.println( "重新下载：:x(" + threadTaskTiles[i].getX() + "),y(" + threadTaskTiles[i].getY() + "),z(" + threadTaskTiles[i].getZoom() + ")" );
						MainTest.addFailedCount();
					}
				}
			}
		} );
	}

	private static void downloadXYZ( Tile tile ) throws Exception {
		String url = "";
		if ( mapType == 1 ) {
			// 地形图的地址
			url = "http://mt3.google.cn/vt/lyrs=t@131,r@234000000&hl=zh-CN&gl=CN&src=app&x={x}&y={y}&z={z}";
		} else if ( mapType == 2 ) {
			// 交通图的地址
			// url = "http://mt1.google.cn/vt/lyrs=a@131,r@234000000&hl=zh-CN&gl=CN&src=app&x={x}&y={y}&z={z}";
			url = "http://t7.tianditu.com/DataServer?T=cva_c&x={x}&y={y}&l={z}";
			// url = "http://mt2.google.cn/vt/lyrs=m@177000000&hl=zh-CN&gl=cn&src=app&x={x}&y={y}&z={z}";
		} else if ( mapType == 3 ) {
			// 遥感地图的地址
			url = "http://mt3.google.cn/vt/lyrs=t@131,r@234000000&hl=zh-CN&gl=CN&src=app&x={x}&y={y}&z={z}";
		}
		if ( mapUrlEnable == 1 ) {
			url = mapUrl;
		}
		url = url.replace( "{x}", tile.getX() + "" ).replace( "{y}", tile.getY() + "" ).replace( "{z}", tile.getZoom() + "" );
		String storePath = downloadDir + "/tiles/" + tile.getZoom() + "/" + tile.getX() + "/" + tile.getY() + ".png";
		storePath = storePath.replace( "//", "/" );
		try {
			DownloadUtil.download( url, storePath, Boolean.parseBoolean( ConfigUtil.get( "isOverride" ) ) );// override
		} catch ( Exception e ) {
			System.err.println( "下载失败：" + storePath );
			throw e;
		}
	}
}
