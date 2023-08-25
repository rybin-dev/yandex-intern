import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class A {

    public static void main(String[] args) {

        InputReader reader = new InputReader(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        Data start = new Data(
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt());

        Data end = new Data(
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt(),
                reader.nextInt());

        int[] res = getDiff(end, start);

        writer.println("%d %d".formatted(res[0], res[1]));

        writer.flush();
    }


    private static class Data {
        public static int[] prefSumMonthDays;

        static {
            prefSumMonthDays = new int[]{0, 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            for (int i = 1; i < prefSumMonthDays.length; i++) {
                prefSumMonthDays[i] += prefSumMonthDays[i - 1];
            }
        }

        private int year;
        private int month;
        private int day;
        private int hour;
        private int min;
        private int sec;

        public Data(int year, int month, int day, int hour, int min, int sec) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
        }

    }

    public static int[] getDiff(Data a, Data b) {
        int dres = (a.year - b.year) * 365;

        int x = ((((((Data.prefSumMonthDays[a.month] + a.day) * 24) + a.hour) * 60) + a.min) * 60) + a.sec;
        int y = ((((((Data.prefSumMonthDays[b.month] + b.day) * 24) + b.hour) * 60) + b.min) * 60) + b.sec;

        if (x < y) {
            x += 365 * 24 * 60 * 60;
            dres -= 365;
        }

        int sr = x - y;

        return new int[]{dres + sr / (24 * 60 * 60), sr % (24 * 60 * 60)};

    }

    static class InputReader {
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
