import java.util.Random;

public class EvaPachi {

    // 初当たり確率設定
    private static final double NORMAL_MODE_PROB = 1.0 / 319.7;  // 通常時の確率 (1/319.7)
    private static final double ST_MODE_PROB = 1.0 / 99.4;     // 確変時 (ST) の確率 (1/99.4)

    // 確率設定（振り分け）
    private static final double TEN_R_PROB = 0.03;  // 10R確変の確率
    private static final double THREE_R_PROB = 0.56; // 3R確変の確率
    private static final double THREE_R_NORMAL_PROB = 0.41; // 3R通常の確率（100% - 10R確変 - 3R確変）

    // 各出玉設定
    private static final int TEN_R_OUTPUT = 1500; // 10R確変出玉
    private static final int THREE_R_OUTPUT = 450; // 3R確変出玉

    private Random random;

    public EvaPachi() {
        random = new Random();
    }

    // 初当たりを決める（通常時とST中の確率）
    public String getInitialDraw(boolean isSTMode) {
        double rand = random.nextDouble();  // 0.0から1.0までのランダムな数を取得

        // 確変中(STモード)ならST確率で初当たりを決定
        if (isSTMode) {
            if (rand < ST_MODE_PROB) {
                return "ST当たり";
            } else {
                return "ST外れ";
            }
        }

        // 通常時の確率
        if (rand < NORMAL_MODE_PROB) {
            return "通常当たり";
        }

        return "外れ";
    }

    // 初当たりの振り分け（確変か通常か）
    public String getDrawResult(String initialDraw, boolean isSTMode) {
        if (initialDraw.equals("ST当たり")) {
            return getBonusOutcome("確変", isSTMode);
        } else if (initialDraw.equals("通常当たり")) {
            return getBonusOutcome("通常", isSTMode);
        } else {
            return getBonusOutcome("外れ", isSTMode);
        }
    }

    // ボーナス結果の表示
    public String getBonusOutcome(String mode, boolean isSTMode) {
        double rand = random.nextDouble();  // 0.0から1.0までのランダムな数を取得

        if (mode.equals("確変")) {
            if (rand < TEN_R_PROB) {
                return "10R確変 (" + TEN_R_OUTPUT + "玉) : 7,7,7";
            } else if (rand < TEN_R_PROB + THREE_R_PROB) {
                return "3R確変 (" + THREE_R_OUTPUT + "玉) : " + getRandomThreeRNum();
            } else {
                return "3R確変 (" + THREE_R_OUTPUT + "玉) : " + getRandomThreeRNum();
            }
        } else if (mode.equals("通常")) {
            if (rand < THREE_R_NORMAL_PROB) {
                return "3R通常 (" + THREE_R_OUTPUT + "玉) : " + getRandomThreeRNormalNum();
            } else {
                return "外れ: 2,4,6";
            }
        } else if (mode.equals("外れ")) {
            return "外れ: 2,4,6";
        } else {
            return "無効";
        }
    }

    // 3R確変時の数字表示（ランダムに1,1,1など）
    private String getRandomThreeRNum() {
        int[] possibleNumbers = {1, 3, 5, 9};  // 確変時の数字の選択肢
        int randIndex = random.nextInt(possibleNumbers.length);
        int number = possibleNumbers[randIndex];
        return number + "," + number + "," + number;
    }

    // 3R通常時の数字表示（ランダムに2,2,2など）
    private String getRandomThreeRNormalNum() {
        int[] possibleNumbers = {2, 4, 6, 8};  // 通常時の数字の選択肢
        int randIndex = random.nextInt(possibleNumbers.length);
        int number = possibleNumbers[randIndex];
        return number + "," + number + "," + number;
    }

    // 実行メイン
    public static void main(String[] args) {
        EvaPachi game = new EvaPachi();
        boolean isSTMode = false;  // 最初は通常時

        // 1回の試行を実行
        String initialDraw = game.getInitialDraw(isSTMode);
        System.out.println("初当たり: " + initialDraw);

        // STモードか通常時かの結果に応じて振り分けを行う
        String bonusOutcome = game.getDrawResult(initialDraw, isSTMode);
        System.out.println("ボーナス結果: " + bonusOutcome);

        // ST突入の場合、STの処理を実行（STモードで1回の抽選）
        if (initialDraw.equals("ST当たり")) {
            isSTMode = true;
            int stCount = 0;

            while (stCount < 163) {
                // ST中の抽選
                String stDraw = game.getInitialDraw(isSTMode);
                System.out.println("ST " + (stCount + 1) + " 回目: " + stDraw);

                if (stDraw.equals("ST当たり")) {
                    bonusOutcome = game.getDrawResult(stDraw, isSTMode);
                    System.out.println("STボーナス結果: " + bonusOutcome);
                    break;
                }

                stCount++;
                if (stCount == 163) {
                    System.out.println("ST終了。通常時に戻ります。");
                    isSTMode = false;  // ST終了後は通常時に戻る
                }
            }
        }
    }
}
