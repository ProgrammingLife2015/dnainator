package nl.tudelft.dnainator.parser;

import java.io.*;

import nl.tudelft.dnainator.parser.buffered.DefaultEdgeParser;

public class EdgeBenchmark {
	static final int NS_PER_MS = 1000000;

	public static void main(String[] args) throws IOException {
		String[] files = {"10000edges.txt", "100000edges.txt", "1000000edges.txt"};
		for (String s : files) {
			System.gc();
			System.gc();
			System.out.println("FileInputStream " + s + " took: " + useFileInputStream(s) + "ms");
			System.gc();
			System.gc();
			System.out.println("DefaultEdgeParser " + s + " took: " + useDefaultEdgeParser(s) + "ms");
			System.gc();
			System.gc();
			System.out.println("BufferedReader " + s + " took: " + useBufferedReader(s) + "ms");
			System.gc();
			System.gc();
			System.out.println("Byte array " + s + " took: " + useFileInputStreamBuffered(s) + "ms");
			System.out.println("------");
		}
	}

	private static long useFileInputStream(String file) throws IOException {
		FileInputStream edgeIn = new FileInputStream(new File(file));
		long time = System.nanoTime();
		String src = "", dst = "";
		boolean wasSpace = false;
		int next;
		while ((next = edgeIn.read()) != -1) {
			if (next == '\n' || next == '\r') {
			    /* TODO: add edge */
				src = dst = "";
				wasSpace = false;
				continue;
			}
			if (next == ' ') {
				wasSpace = true;
				continue;
			}
			if (wasSpace) {
				src += (char) next;
			} else {
				dst += (char) next;
			}
		}
		edgeIn.close();
		return (System.nanoTime() - time) / NS_PER_MS;
	}

	private static long useFileInputStreamBuffered(String file) throws IOException {
		File f = new File(file);
		FileInputStream fIn = new FileInputStream(f);
		long time = System.nanoTime();

		String src = "", dst = "";
		char cur;
		boolean wasSpace = false;

		byte[] bytes = new byte[(int) f.length()];
		fIn.read(bytes);

		if (f.length() > Integer.MAX_VALUE - 8) {
			System.err.println("TOO LARGE TO HANDLE!");
			return -1;
		}

		for (byte b : bytes) {
			cur = (char) b;
			if (cur == '\n' || cur == '\r') {
			    /* TODO: add edge */
				src = dst = "";
				wasSpace = false;
				continue;
			}
			if (cur == ' ') {
				wasSpace = true;
				continue;
			}
			if (wasSpace) {
				src += (char) cur;
			} else {
				dst += (char) cur;
			}
		}
		fIn.close();
		return (System.nanoTime() - time) / NS_PER_MS;
	}

	private static long useBufferedReader(String file) throws IOException {
		File f = new File(file);
		BufferedReader br = new BufferedReader(new FileReader(f));
		long time = System.nanoTime();

		String src = "", dst = "";
		boolean wasSpace = false;

		char[] chars = new char[(int) f.length()];
		br.read(chars);

		if (f.length() > Integer.MAX_VALUE - 8) {
			System.err.println("TOO LARGE TO HANDLE!");
			return -1;
		}

		for (char cur : chars) {
			if (cur == '\n' || cur == '\r') {
			    /* TODO: add edge */
				src = dst = "";
				wasSpace = false;
				continue;
			}
			if (cur == ' ') {
				wasSpace = true;
				continue;
			}
			if (wasSpace) {
				src += (char) cur;
			} else {
				dst += (char) cur;
			}
		}
		br.close();
		return (System.nanoTime() - time) / NS_PER_MS;
	}

	private static long useDefaultEdgeParser(String file) throws IOException {
			File f = new File(file);
			BufferedReader in = new BufferedReader(new FileReader(f));
			long time = System.nanoTime();
			EdgeParser ep = new DefaultEdgeParser(in);
			while (ep.hasNext()) {
				ep.next();
			}
			return (System.nanoTime() - time)/NS_PER_MS;
	}
}
