import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class B {

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        Game game = new Game();

        int n = reader.nextInt();
        int m = reader.nextInt();
        int q = reader.nextInt();

        for (int i = 0; i < n; i++) {
            game.addCard('A', reader.nextInt());
        }

        for (int i = 0; i < m; i++) {
            game.addCard('B', reader.nextInt());
        }

        for (int i = 0; i < q; i++) {
            game.execute(reader.nextInt(), reader.nextString(), reader.nextInt());
            writer.print(game.getDiversity());
            writer.print(' ');
        }


        writer.flush();
    }

    private static class Game {
        private Map<Integer, Integer> playerA = new HashMap<>();
        private Map<Integer, Integer> playerB = new HashMap<>();
        private int diversity;

        public int getDiversity(){
            return this.diversity;
        }

        public void execute(int type, String player, int card) {
            if (type == 1) {
                addCard(player.charAt(0), card);
            }
            if (type == -1) {
                removeCard(player.charAt(0), card);
            }

        }

        private void addCard(char player, int card) {

            if (player == 'A') {
                _addCard(playerA, playerB, card);
            }
            else if (player == 'B') {
                _addCard(playerB, playerA, card);
            }

        }

        private void _addCard(Map<Integer, Integer> playerA, Map<Integer, Integer> playerB, int card) {
            int countCardA = playerA.merge(card, 1, Integer::sum);
            int countCardB = playerB.getOrDefault(card, 0);
            diversity += countCardA > countCardB ? 1 : -1;
        }

        private void removeCard(char player, int card) {
            if (player == 'A') {
                _removeCard(playerA, playerB, card);
            }
            else if (player == 'B') {
                _removeCard(playerB, playerA, card);
            }
        }

        private void _removeCard(Map<Integer, Integer> playerA, Map<Integer, Integer> playerB, int card) {
            int countCardA = playerA.merge(card, 1, (oldValue, value) -> oldValue - value);
            int countCardB = playerB.getOrDefault(card, 0);
            diversity += countCardA < countCardB ? 1 : -1;
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
