import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class Main {
	static InputStream is;
	static PrintWriter out;
	static String INPUT = "";
	
	static void solve()
	{
		int n = ni(), f = ni(), m = ni();
		int[][] sp = new int[m][7];
		for(int i = 0;i < m;i++){
			for(int j = 0;j < 5;j++){
				sp[i][j] = ni();
			}
			sp[i][0]--;
			sp[i][3]--;
		}
		int[][][] t = new int[n][m+2][3];
		int[] tp = new int[n];
		Arrays.fill(tp, 2);
		for(int i = 0;i < n;i++){
			t[i][0][0] = 1;
			t[i][0][1] = -1;
			t[i][1][0] = f;
			t[i][1][1] = -1;
		}
		for(int i = 0;i < m;i++){
			int a = sp[i][0];
			int b = sp[i][1];
			t[a][tp[a]][0] = b;
			t[a][tp[a]][1] = i;
			t[a][tp[a]][2] = 0;
			tp[a]++;
			int c = sp[i][3];
			int d = sp[i][4];
			t[c][tp[c]][0] = d;
			t[c][tp[c]][1] = i;
			t[c][tp[c]][2] = 1;
			tp[c]++;
		}
		
		for(int i = 0;i < n;i++){
			Arrays.sort(t[i], 1, tp[i], new Comparator<int[]>(){
				public int compare(int[] a, int[] b){
					return a[0] - b[0];
				}
			});
		}
		int[] head = new int[n+1];
		for(int i = 1;i <= n;i++){
			head[i] = head[i-1] + tp[i-1];
		}
		
		for(int i = 0;i < n;i++){
			for(int j = 1;j < tp[i];j++){
				if(t[i][j][1] >= 0){
					sp[t[i][j][1]][5+t[i][j][2]] = head[i]+j;
				}
			}
		}
		
		int[] from = new int[10000];
		int[] to = new int[10000];
		int[] w = new int[10000];
		int p = 0;
		for(int i = 0;i < n;i++){
			for(int j = 0;j < tp[i]-1;j++){
				from[p] = head[i]+j;
				to[p] = head[i]+j+1;
				w[p] = t[i][j+1][0]-t[i][j][0];
				p++;
			}
		}
		for(int i = 0;i < n-1;i++){
			from[p] = head[i];
			to[p] = head[i+1];
			w[p] = 1;
			p++;
		}
		for(int i = 0;i < m;i++){
			from[p] = sp[i][5];
			to[p] = sp[i][6];
			w[p] = sp[i][2];
			p++;
		}
//		tr(Arrays.copyOf(from, p));
//		tr(Arrays.copyOf(to, p));
//		tr(Arrays.copyOf(w, p));
		
		int[][][] g = packWU(head[n], from, to, w, p);
		
		int q = ni();
		for(int i = 0;i < q;i++){
			int bq = ni()-1, fq = ni();
			final int[] td = new int[head[n]];
			Arrays.fill(td, Integer.MAX_VALUE);
			
			TreeSet<Integer> ts = new TreeSet<Integer>(new Comparator<Integer>() {
				public int compare(Integer a, Integer b) {
					if (td[a] - td[b] != 0)
						return td[a] - td[b];
					return a - b;
				}
			});
			
			for(int j = 0;j < tp[bq];j++){
				int ind = head[bq] + j;
				td[ind] = Math.abs(fq-t[bq][j][0]);
				ts.add(ind);
			}

			while (ts.size() > 0) {
				int cur = ts.pollFirst();

				for (int j = 0; j < g[cur].length; j++) {
					int next = g[cur][j][0];
					int nd = td[cur] + g[cur][j][1];
					if (nd < td[next]) {
						ts.remove(next);
						td[next] = nd;
						ts.add(next);
					}
				}
			}
			
			long sum = 0;
			for(int j = 0;j < n;j++){
				if(j == bq)continue;
				for(int k = 0;k < tp[j];k++){
					sum += td[head[j]+k];
				}
				for(int k = 0;k < tp[j]-1;k++){
					sum += sum(td[head[j]+k], td[head[j]+k+1], t[j][k+1][0]-t[j][k][0]);
				}
			}
			
			out.println(sum);
		}
	}
	
	static long sum(int a, int b, int c)
	{
		int m = (a+b+c)/2;
		if((a+b+c)%2 == 0){
			return (long)(m+a)*(m-a+1)/2+(long)(m+b)*(m-b+1)/2-m-a-b;
		}else{
			return (long)(m+a)*(m-a+1)/2+(long)(m+b)*(m-b+1)/2-a-b;
		}
	}
	
	static int md(int a, int b, int c)
	{
		// a+d=b+e=?
		// d+e=c
		// 2d=b+c-a
		// d=(b+c-a)/2
		// ?=(a+b+c)/2
		
		return (a+b+c)/2;
	}
	
	public static int[][][] packWU(int n, int[] from, int[] to, int[] w, int sup)
	{
		int[][][] g = new int[n][][];
		int[] p = new int[n];
		for(int i = 0;i < sup;i++)p[from[i]]++;
		for(int i = 0;i < sup;i++)p[to[i]]++;
		for(int i = 0;i < n;i++)g[i] = new int[p[i]][2];
		for(int i = 0;i < sup;i++){
			--p[from[i]];
			g[from[i]][p[from[i]]][0] = to[i];
			g[from[i]][p[from[i]]][1] = w[i];
			--p[to[i]];
			g[to[i]][p[to[i]]][0] = from[i];
			g[to[i]][p[to[i]]][1] = w[i];
		}
		return g;
	}
	
	public static void main(String[] args) throws Exception
	{
//		int n = 100, m = 100, f = 1000000;
//		Random gen = new Random();
//		StringBuilder sb = new StringBuilder();
//		sb.append(n + " ");
//		sb.append(f + " ");
//		sb.append(m + " ");
//		for (int i = 0; i < m; i++) {
//			sb.append(gen.nextInt(n)+1 + " ");
//			sb.append(gen.nextInt(f)+1 + " ");
//			sb.append(gen.nextInt(f)+1 + " ");
//			sb.append(gen.nextInt(n)+1 + " ");
//			sb.append(gen.nextInt(f)+1 + " ");
//		}
//		
//		sb.append(2012 + " ");
//		for(int i = 0;i < 2012;i++){
//			sb.append(gen.nextInt(n)+1 + " ");
//			sb.append(gen.nextInt(f)+1 + " ");
//		}
//		INPUT = sb.toString();
		
		long S = System.currentTimeMillis();
		is = INPUT.isEmpty() ? System.in : new ByteArrayInputStream(INPUT.getBytes());
		out = new PrintWriter(System.out);
		
		solve();
		out.flush();
		long G = System.currentTimeMillis();
		tr(G-S+"ms");
	}
	
	static boolean eof()
	{
		try {
			is.mark(1000);
			int b;
			while((b = is.read()) != -1 && !(b >= 33 && b <= 126));
			is.reset();
			return b == -1;
		} catch (IOException e) {
			return true;
		}
	}
		
	static int ni()
	{
		try {
			int num = 0;
			boolean minus = false;
			while((num = is.read()) != -1 && !((num >= '0' && num <= '9') || num == '-'));
			if(num == '-'){
				num = 0;
				minus = true;
			}else{
				num -= '0';
			}
			
			while(true){
				int b = is.read();
				if(b >= '0' && b <= '9'){
					num = num * 10 + (b - '0');
				}else{
					return minus ? -num : num;
				}
			}
		} catch (IOException e) {
		}
		return -1;
	}
	
	static void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
}
