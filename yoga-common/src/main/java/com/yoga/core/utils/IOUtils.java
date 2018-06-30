package com.yoga.core.utils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

	private IOUtils() {
		throw new AssertionError();
	}

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				throw new RuntimeException("IOException occurred. ", e);
			}
		}
	}
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
			}
		}
	}

}
