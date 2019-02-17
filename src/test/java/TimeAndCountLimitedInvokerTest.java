import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeAndCountLimitedInvokerTest {
    final boolean[] isTrigged = {false};
    TimeAndCountLimitedInvoker invoker;

    @Before
    public void setUp() throws Exception {
        isTrigged[0] = false;
        invoker = new TimeAndCountLimitedInvoker(3, 4000, () -> {
            isTrigged[0] = true;
            return null;
        });
    }

    @Test
    public void triggerInSpecificCount() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            invoker.trigger();
        }
        assertTrue(isTrigged[0]);
    }

    @Test
    public void triggerInSpecificTime() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            if (i > 1)
                Thread.sleep(2000);
            
            invoker.trigger();
        }
        assertTrue(isTrigged[0]);
    }

    @Test
    public void notTriggerInSpecificTime() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            invoker.trigger();
            Thread.sleep(2000);
        }
        assertFalse(isTrigged[0]);
    }
}