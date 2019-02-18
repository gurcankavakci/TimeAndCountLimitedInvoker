import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class TimeAndCountLimitedInvoker {
    private TriggerQueue<Long> queue;
    private int msecond;
    private Calendar calendar;
    private Runnable methodToTrigger;

    public TimeAndCountLimitedInvoker(int triggerCount, int delayMSec, Runnable methodToTrigger) {
        this.queue = new TriggerQueue(triggerCount);
        this.msecond = delayMSec;
        this.methodToTrigger = methodToTrigger;
    }

    public TimeAndCountLimitedInvoker(int triggerCount, int delayMSec) {
        this(triggerCount, delayMSec, null);
    }

    public void trigger() {
        if (triggerAndCheck() && methodToTrigger != null) {
            methodToTrigger.run();
        }
    }

    public void trigger(Runnable runnable) {
        if (triggerAndCheck()) {
            runnable.run();
        }
    }

    private boolean triggerAndCheck() {
        calendar = Calendar.getInstance();
        queue.add(calendar.getTimeInMillis());
        calendar.add(Calendar.MILLISECOND, -msecond);
        return queue.isTriggerLimitFull() && queue.peek() > calendar.getTimeInMillis();
    }

    private static class TriggerQueue<T> extends CircularFifoQueue<T> {
        public TriggerQueue(int size) {
            super(size);
        }

        public boolean isTriggerLimitFull() {
            return this.size() == this.maxSize();
        }
    }
}
