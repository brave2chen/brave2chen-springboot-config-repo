package jdk8;

import java.util.ArrayList;
import java.util.List;

/**
 * message
 *
 * @author 陈庆勇
 * @date 2016年11月2日
 */
interface Face {
	int add(int a, int b);
	
	default int div(int a, int b){
		return a - b;
	}
	
	static List<Integer> data = new ArrayList<>();
	static void add(int i) {
	    data.add(i);
	}
	
	static int get(int i) {
	    return data.get(i);
	}
}

public class FaceImpl implements Face{
	/* 
	 * @see jdk8.Face#add(int, int)
	 */
	@Override
	public int add(int a, int b) {
		return a + b;
	}
	
	public static void main(String[] args) {
	    FaceImpl faceImpl = new FaceImpl();
		System.out.println(faceImpl.add(2, 5));
		System.out.println(faceImpl.div(6, 3));
	}
}

