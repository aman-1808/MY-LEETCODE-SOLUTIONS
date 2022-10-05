import java.io.*;

import java.util.*;

public class Main {

	 public static void main(String[] args) throws Exception {

	        InputReader in = new InputReader(System.in);

	        OutputWriter out = new OutputWriter(System.out);

	        int t = in.readInt();

			Task Solver = new Task();

			while(t-- > 0)

				Solver.solve(in,out);

			out.close();

	 }

}

class Task{

	long l, r, k;

	ArrayList<Integer> upto = new ArrayList<Integer>();

	long dp[][][][] = new long[2][20][504][(1 << 9) + 5];

	public void solve(InputReader in, OutputWriter out) {

		l = in.readLong();

		r = in.readLong();

		k = in.readLong();

		long ans = 0;

		set_array(r);

		ans += find(1,0,0,0,0);

		set_array(l - 1);

		ans -= find(1,0,0,0,0);

		out.printLine(ans);

	}

	private void set_array(long k) {

		upto.removeAll(upto);

		if(k == 0) upto.add((int)k);

		else {

			while(k > 0) {

				upto.add((int)(k % 10));

				k /= 10;

			}

			Collections.reverse(upto);

		}

		for(int i = 0; i < 2; ++i) {

			for(int j = 0; j < 20; ++j) {

				for(int x = 0; x < 504; ++x) Arrays.fill(dp[i][j][x], -1);

			}

		}

	}

	private long find(int tight, int pos, int mod, int mask, int last) {

		if(pos == upto.size()) {

			int cnt = 0;

			for(int i = 1; i < 10; ++i) {

				if(i == 5) continue;

				if(((mask) & (1 << (i - 1))) > 0)

					if(mod % i == 0) cnt++;

			}

			if(((mask & (1 << 4)) > 0) && (last % 5 == 0)) cnt++;

			return (cnt >= k) ? 1 : 0;

		}

		if(dp[tight][pos][mod][mask] != -1) return dp[tight][pos][mod][mask];

		int end = (tight == 1) ? upto.get(pos): 9;

		long ret = 0;

		for(int i = 0; i <= end; ++i) {

			if(i == 0) 

				ret += find(tight & ((i == end) ? 1 : 0), pos + 1, (mod * 10) % 504, mask, i);

			else ret += find(tight & ((i == end) ? 1 : 0), pos + 1, (mod * 10 + i) % 504, mask | (1 << (i - 1)), i);

		}

		return dp[tight][pos][mod][mask] = ret;

	}

}

class InputReader {

	private InputStream stream;

	private byte[] buf = new byte[1024];

	private int curChar;

	private int numChars;

	private SpaceCharFilter filter;

	public InputReader(InputStream stream) {

		this.stream = stream;

	}

	public int read() {

		if (numChars == -1) {

			throw new InputMismatchException();

		}

		if (curChar >= numChars) {

			curChar = 0;

			try {

				numChars = stream.read(buf);

			} catch (IOException e) {

				throw new InputMismatchException();

			}

			if (numChars <= 0) {

				return -1;

			}

		}

		return buf[curChar++];

	}

	public int readInt() {

		int c = read();

		while (isSpaceChar(c)) {

			c = read();

		}

		int sgn = 1;

		if (c == '-') {

			sgn = -1;

			c = read();

		}

		int res = 0;

		do {

			if (c < '0' || c > '9') {

				throw new InputMismatchException();

			}

			res *= 10;

			res += c - '0';

			c = read();

		} while (!isSpaceChar(c));

		return res * sgn;

	}

	public String readString() {

		int c = read();

		while (isSpaceChar(c)) {

			c = read();

		}

		StringBuilder res = new StringBuilder();

		do {

			res.appendCodePoint(c);

			c = read();

		} while (!isSpaceChar(c));

		return res.toString();

	}

	public double readDouble() {

		int c = read();

		while (isSpaceChar(c)) {

			c = read();

		}

		int sgn = 1;

		if (c == '-') {

			sgn = -1;

			c = read();

		}

		double res = 0;

		while (!isSpaceChar(c) && c != '.') {

			if (c == 'e' || c == 'E') {

				return res * Math.pow(10, readInt());

			}

			if (c < '0' || c > '9') {

				throw new InputMismatchException();

			}

			res *= 10;

			res += c - '0';

			c = read();

		}

		if (c == '.') {

			c = read();

			double m = 1;

			while (!isSpaceChar(c)) {

				if (c == 'e' || c == 'E') {

					return res * Math.pow(10, readInt());

				}

				if (c < '0' || c > '9') {

					throw new InputMismatchException();

				}

				m /= 10;

				res += (c - '0') * m;

				c = read();

			}

		}

		return res * sgn;

	}

	public long readLong() {

		int c = read();

		while (isSpaceChar(c)) {

			c = read();

		}

		int sgn = 1;

		if (c == '-') {

			sgn = -1;

			c = read();

		}

		long res = 0;

		do {

			if (c < '0' || c > '9') {

				throw new InputMismatchException();

			}

			res *= 10;

			res += c - '0';

			c = read();

		} while (!isSpaceChar(c));

		return res * sgn;

	}

	

	public boolean isSpaceChar(int c) {

		if (filter != null) {

			return filter.isSpaceChar(c);

		}

		return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;

	}

	public String next() {

		return readString();

	}

	

	public interface SpaceCharFilter {

		public boolean isSpaceChar(int ch);

	}

}

class OutputWriter {

	private final PrintWriter writer;

	

	public OutputWriter(OutputStream outputStream) {

		writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));

	}

	public OutputWriter(Writer writer) {

		this.writer = new PrintWriter(writer);

	}

	public void print(Object... objects) {

		for (int i = 0; i < objects.length; i++) {

			if (i != 0) {

				writer.print(' ');

			}

			writer.print(objects[i]);

		}

		writer.flush();

	}

	public void printLine(Object... objects) {

		print(objects);

		writer.println();

		writer.flush();

	}

	public void close() {

		writer.close();

	}

	public void flush() {

		writer.flush();

	}

}
