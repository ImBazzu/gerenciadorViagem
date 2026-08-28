import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;

public class main {

    private static final Robot robor;

    static {
        try {
            robor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws AWTException, InterruptedException {

        apertarESoltarBotao(new int[]{KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_F12});

        Thread.sleep(Duration.ofSeconds(5).toMillis());
        apertarESoltarBotao(new int[]{KeyEvent.VK_ENTER});

        Thread.sleep(Duration.ofSeconds(5).toMillis());

        apertarESoltarBotao(new int[]{KeyEvent.VK_R,
                KeyEvent.VK_O, KeyEvent.VK_O, KeyEvent.VK_T,});

        apertarESoltarBotao(new int[]{KeyEvent.VK_ENTER});

        Thread.sleep(Duration.ofSeconds(5).toMillis());

        robor.keyPress(KeyEvent.VK_SHIFT);
        apertarESoltarBotao(new int[]{KeyEvent.VK_2,KeyEvent.VK_R});
        robor.keyRelease(KeyEvent.VK_SHIFT);
        apertarESoltarBotao(new int[]{KeyEvent.VK_P,KeyEvent.VK_A,KeyEvent.VK_V,KeyEvent.VK_O,
                KeyEvent.VK_U,KeyEvent.VK_E,KeyEvent.VK_T,KeyEvent.VK_M,KeyEvent.VK_4,KeyEvent.VK_2});
        apertarESoltarBotao(new int[]{KeyEvent.VK_ENTER});



    }

    public static void apertarESoltarBotao(int[] e) throws AWTException {
        for (int j : e) {
            robor.keyPress(j);
        }
        for (int j : e) {
            robor.keyRelease(j);
        }
    }
}
