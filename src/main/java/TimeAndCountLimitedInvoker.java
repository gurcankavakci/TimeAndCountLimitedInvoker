import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.Calendar;
import java.util.concurrent.Callable;

public class TimeAndCountLimitedInvoker {
    private TriggerQueue<Long> queue;
    private int msecond;
    private Calendar calendar;
    private Callable methodToTrigger;

    public TimeAndCountLimitedInvoker(int triggerCount, int delayMSec, Callable methodToTrigger) {
        this.queue = new TriggerQueue(triggerCount);
        this.msecond = delayMSec;
        this.methodToTrigger = methodToTrigger;
    }


    public void trigger() {
        calendar = Calendar.getInstance();
        queue.add(calendar.getTimeInMillis());

        calendar.add(Calendar.MILLISECOND, -msecond);
        if (queue.isTriggerLimitFull() && queue.peek() > calendar.getTimeInMillis()) {
            try {
                methodToTrigger.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class TriggerQueue<T> extends CircularFifoQueue<T>{
        public TriggerQueue(int size) {
            super(size);
        }

        public boolean isTriggerLimitFull() {
            return this.size() == this.maxSize();
        }
    }
}
