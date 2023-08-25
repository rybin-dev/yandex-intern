import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.EmptyStackException;

public class D {
    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = reader.nextInt();

        String[] employees = new String[n + 1];
        int[] a = new int[n + 1];
        int[] b = new int[n + 1];


        for (int i = 1; i < n + 1; i++) {
            switch (reader.nextString()) {
                case "A" -> {
                    employees[i] = "A";
                    a[i] = 0;
                    b[i] = 1;
                }
                case "B" -> {
                    employees[i] = "B";
                    a[i] = 1;
                    b[i] = 0;
                }

            }
        }
        int[] res = new int[n + 1];
        Stack stack = new Stack(2 * (n + 1));

        stack.push(reader.nextInt());

        for (int i = 1; i < 2 * (n + 1); i++) {

            int chief = stack.peek();
            int employee = reader.nextInt();

            if (chief == employee) {
                stack.pop();
                continue;
            }

            if (employees[employee].equals("A")) {
                res[employee] = a[chief];
                b[employee] += b[chief];
            }
            if (employees[employee].equals("B")) {
                res[employee] = b[chief];
                a[employee] += a[chief];
            }

            stack.push(employee);
        }

        for (int i = 1; i < n+1 ; i++) {
            writer.print(res[i]);
            writer.print(' ');
        }
        writer.flush();
    }

    private static class Stack {
        private int[] elementData;
        private final int capacity;

        private int elementCount;

        public Stack(int capacity) {
            this.capacity = capacity;

            elementData = new int[capacity];
        }

        public int push(int e) {
            elementData[elementCount++] = e;
            return e;
        }

        public int pop() {
            int e = peek();
            elementCount--;
            return e;
        }

        public int peek() {
            if (elementCount == 0) {
                throw new EmptyStackException();
            }

            return elementData[elementCount-1];
        }

        public boolean isEmpty() {
            return elementCount == 0;
        }
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