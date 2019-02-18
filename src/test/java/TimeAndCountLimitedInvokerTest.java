import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TimeAndCountLimitedInvokerTest {
    final boolean[] isTrigged = {false};
    TimeAndCountLimitedInvoker invoker;
    TimeAndCountLimitedInvoker invoker2;

    @Before
    public void setUp() throws Exception {
        isTrigged[0] = false;
        invoker = new TimeAndCountLimitedInvoker(3, 1000, () -> {
            isTrigged[0] = true;
        });

        invoker2 = new TimeAndCountLimitedInvoker(3, 1000);
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
                Thread.sleep(500);
            
            invoker.trigger();
        }
        assertTrue(isTrigged[0]);
    }

    @Test
    public void notTriggerInSpecificTime() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            invoker.trigger();
            Thread.sleep(500);
        }
        assertFalse(isTrigged[0]);
    }

    @Test
    public void trigger2InSpecificCount() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            invoker.trigger(() -> isTrigged[0] = true);
        }
        assertTrue(isTrigged[0]);
    }

    @Test
    public void notTrigger2InSpecificTime() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            invoker.trigger(() -> isTrigged[0] = true);
            Thread.sleep(500);
        }
        assertFalse(isTrigged[0]);
    }
}