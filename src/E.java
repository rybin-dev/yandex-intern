import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class E {
    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = reader.nextInt();
        int[][] arrs = new int[n][];

        for (int i = 0; i < n; i++) {
            int len = reader.nextInt();
            int[] arr = new int[len];
            for (int j = 0; j < len; j++) {
                arr[j] = reader.nextInt();
            }
            arrs[i] = arr;
        }

        writer.println(start(arrs));
        writer.flush();
    }

    public static long start(int[][] arrs) {
        return Arrays.stream(arrs)
                .collect(Collectors.groupingBy(arr -> arr[0]))
                .values()
                .stream()
                .filter(list -> list.size() > 1)
                .mapToLong(list -> sum( 1, list))
                .sum();
    }

    private static long sum(int column, List<int[]> rows) {
        return rows
                .stream()
                .filter(row -> row.length > column)
                .collect(Collectors.groupingBy(row -> row[column], Collectors.toList()))
                .values()
                .stream()
                .filter(list -> list.size() > 1)
                .mapToLong(list -> sum(column + 1, list))
                .sum() + calc(rows.size());

    }

    private static long calc(long n) {
        return ((n * (n - 1)) / 2);
    }

    private static class InputReader {
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
                throw new UnknownError();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new UnknownError();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int nextInt() {
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
                    throw new UnknownError();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String nextString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return nextString();
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}