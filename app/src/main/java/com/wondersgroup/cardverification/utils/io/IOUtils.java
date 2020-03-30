package com.wondersgroup.cardverification.utils.io;

import com.blankj.utilcode.util.LogUtils;

import java.io.Closeable;
import java.io.IOException;


public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
