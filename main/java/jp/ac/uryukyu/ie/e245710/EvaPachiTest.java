import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EvaPachiTest {

    @Test
    public void testInitialDraw() {
        EvaPachi game = new EvaPachi();
        String drawResult = game.getInitialDraw(false);  // 通常時の初当たり試行
        assertNotNull(drawResult);
    }

    @Test
    public void testSTModeTransition() {
        EvaPachi game = new EvaPachi();
        String initialDraw = game.getInitialDraw(false);
        if (initialDraw.equals("ST当たり")) {
            assertTrue(game.runSTMode());  // ST突入後の試行が成功
        }
    }

    @Test
    public void testHandsDepletion() {
        EvaPachi game = new EvaPachi();
        // 手持ち玉を15玉未満にする
        game.getInitialDraw(false);  // 1回試行
        assertTrue(game.getHands() < 15);
    }
}
