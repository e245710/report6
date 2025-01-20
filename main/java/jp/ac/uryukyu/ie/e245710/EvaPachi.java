import java.util.Random;

public class EvaPachi {

    private static final double NORMAL_MODE_PROB = 1.0 / 319.7;  // 通常時の確率 (1/319.7)
    private static final double ST_MODE_PROB = 1.0 / 99.4;     // 確変時 (ST) の確率 (1/99.4)
    private static final double TEN_R_PROB = 0.03;  // 10R確変の確率
    private static final double THREE_R_PROB = 0.56; // 3R確変の確率
    private static final double THREE_R_NORMAL_PROB = 0.41; // 3R通常の確率（100% - 10R確変 - 3R確変）
    private static final int TEN_R_OUTPUT = 1500; // 10R確変出玉
    private static final int THREE_R_OUTPUT = 450; // 3R確変出玉
    private static final int THREE_R_NORMAL_OUTPUT = 450; // 3R通常出玉
    private static final int INITIAL_HANDS = 10000;  // 初期持ち玉
    private int hands;  // 現在の持ち玉
    private Random random;

    public EvaPachi() {
        random = new Random();
        this.hands = INITIAL_HANDS;
    }

    // 初当たりを抽選する
    public String getInitialDraw() {
        if (hands < 15) {
            return "持ち玉が不足しています";
        }
        hands -= 15; // 1回の抽選に15玉消費
        double rand = random.nextDouble();  // 0.0から1.0までのランダムな数を取得

        // 初回抽選で1/319.7の確率で当たりが引かれる
        if (rand < NORMAL_MODE_PROB) {
            return "外れ";  // 通常の外れの場合
        }

        // 振り分け
        if (rand < THREE_R_PROB) {
            return "3R確変";  // 3R確変（56%）
        } else if (rand < THREE_R_PROB + TEN_R_PROB) {
            return "10R確変";  // 10R確変（3%）
        } else {
            return "外れ";  // 外れ（通常当たり）
        }
    }

    // ボーナス結果を決める
    public String getBonusOutcome(String mode) {
        double rand = random.nextDouble();  // 0.0から1.0までのランダムな数を取得

        if (mode.equals("3R確変")) {
            hands += THREE_R_OUTPUT;  // 3R確変の出玉
            return "3R確変 (" + THREE_R_OUTPUT + "玉) : " + getRandomThreeRNum() + " -> ST突入";
        } else if (mode.equals("10R確変")) {
            hands += TEN_R_OUTPUT;  // 10R確変の出玉
            return "10R確変 (" + TEN_R_OUTPUT + "玉) : 7,7,7 -> ST突入";
        } else if (mode.equals("外れ")) {
            return "外れ: 2,4,6";
        }
        return "無効";
    }

    // STモードを実行する
    public void runSTMode() {
        int stCount = 0;

        // STモード中の試行 (最大163回)
        while (stCount < 163) {
            double rand = random.nextDouble();
            if (rand < ST_MODE_PROB) {  // 1/99.4 の確率でST当たり
                hands += TEN_R_OUTPUT;  // 10R確変の出玉
                System.out.println("ST中: 10R確変 (" + TEN_R_OUTPUT + "玉) : 7,7,7 -> ST継続");
                stCount = 0;  // STモード継続
            } else {
                stCount++;  // 当たらなければ回数をカウント
            }

            // 持ち玉が不足していた場合、終了
            if (hands < 15) {
                System.out.println("持ち玉が不足しています。ゲーム終了。");
                return;
            }
        }

        // STモード終了後、通常確率に戻る
        System.out.println("STモード終了。現在の持ち玉: " + hands);
    }

    // 手持ち玉を取得
    public int getHands() {
        return hands;
    }

    // 3R確変時の数字表示（ランダムに1,1,1など）
    private String getRandomThreeRNum() {
        int[] possibleNumbers = {1, 3, 5, 9};  // 確変時の数字の選択肢
        int randIndex = random.nextInt(possibleNumbers.length);
        int number = possibleNumbers[randIndex];
        return number + "," + number + "," + number;
    }

    // メインの実行部分
    public static void main(String[] args) {
        EvaPachi game = new EvaPachi();
        boolean gameActive = true;

        // 初当たりを決定する
        String initialDraw = game.getInitialDraw();
        System.out.println("初当たり: " + initialDraw);

        if (initialDraw.equals("持ち玉が不足しています")) {
            gameActive = false;
        } else {
            String bonusOutcome = game.getBonusOutcome(initialDraw);
            System.out.println(bonusOutcome);

            // STモードに突入する場合
            if (initialDraw.equals("3R確変") || initialDraw.equals("10R確変")) {
                game.runSTMode();
            } else {
                // 通常当たり（STモードに入らない）
                System.out.println("通常当たり: 継続");
            }
        }

        if (gameActive) {
            System.out.println("ゲーム終了。最終持ち玉: " + game.getHands());
        }
    }
}
