import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class C {
    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = reader.nextInt();
        int m = reader.nextInt();
        int q = reader.nextInt();


        Map<String, Integer> columns = new HashMap<>();
        int[][] table = new int[n][m];

        for (int i = 0; i < m; i++) {
            columns.put(reader.nextString(), i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                table[i][j] = reader.nextInt();
            }
        }


        Map<String, int[]> filters = new HashMap<>();

        for (int i = 0; i < q; i++) {
            String column = reader.nextString();
            String predicate = reader.nextString();
            int val = reader.nextInt();

            if ("<".equals(predicate)) {
                filters.merge(
                        column,
                        new int[]{val, Integer.MIN_VALUE},
                        (oldValue, value) -> {
                            oldValue[0] = Math.min(oldValue[0], value[0]);
                            return oldValue;
                        });
            } else {
                filters.merge(
                        column,
                        new int[]{Integer.MAX_VALUE, val},
                        (oldValue, value) -> {
                            oldValue[1] = Math.max(oldValue[1], value[1]);
                            return oldValue;
                        });
            }
        }


        Predicate<int[]> predicate = (e) -> true;

        for (Map.Entry<String, int[]> entry : filters.entrySet()) {
            String k = entry.getKey();
            int[] v = entry.getValue();

            predicate = predicate.and(row -> {
                int indexColum = columns.get(k);
                return row[indexColum] < v[0] && row[indexColum] > v[1];
            });
        }


        long l = Arrays.stream(table)
                .filter(predicate)
                .flatMapToInt(Arrays::stream)
                .mapToLong(e -> (long) e)
                .sum();

        writer.println(l);

        writer.flush();
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
