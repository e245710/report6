public class Main {
    public static void main(String[] args) {
        EvaPachi game = new EvaPachi();
        boolean isSTMode = false;

        // 持ち玉が尽きるかST突入が終わるまで繰り返す
        while (game.getHands() > 0) {
            String initialDraw = game.getInitialDraw(isSTMode);
            System.out.println("初当たり: " + initialDraw);

            String bonusOutcome = game.getDrawResult(initialDraw, isSTMode);
            System.out.println("ボーナス結果: " + bonusOutcome);

            // 持ち玉が尽きた場合
            if (initialDraw.equals("持ち玉が不足しています")) {
                System.out.println("持ち玉が尽きました。最終出玉: " + game.getHands() + "玉");
                break;
            }

            // ST突入後、最大163回の試行
            if (initialDraw.equals("ST当たり")) {
                isSTMode = true;
                boolean isSTContinued = game.runSTMode();
                if (!isSTContinued) {
                    System.out.println("ST終了。通常時に戻ります。最終出玉: " + game.getHands() + "玉");
                    break;
                }
            }
        }
    }
}
