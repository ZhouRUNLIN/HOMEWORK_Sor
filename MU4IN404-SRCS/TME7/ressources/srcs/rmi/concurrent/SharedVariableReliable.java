package srcs.rmi.concurrent;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedVariableReliable<T extends Serializable> implements SharedVariable<T> {
    private T attr;

    private boolean locked = false; // 用于表示当前变量是否被锁定
    private final Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private Timer timer = new Timer();
    private TimerTask unlockTask; // 新增一个TimerTask成员，用于取消之前的任务

    public SharedVariableReliable(T attr) {
        this.attr = attr;
    }

    @Override
    public T obtenir() throws RemoteException {
        lock.lock();
        try {
            while (locked) {
                condition.await();
            }
            locked = true;
            // 每次在设置新任务之前取消之前的任务
            if (unlockTask != null) {
                unlockTask.cancel();
            }
            unlockTask = new TimerTask() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        locked = false;
                        condition.signalAll();
                    } finally {
                        lock.unlock();
                    }
                }
            };
            timer.schedule(unlockTask, 2500); // 重新调度定时器任务
            return attr;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void relacher(T x) throws RemoteException {
        lock.lock();
        try {
            attr = x;
            locked = false;
            if (unlockTask != null) {
                unlockTask.cancel(); // 在手动释放锁时取消自动释放任务
                unlockTask = null; // 清除引用，以便垃圾回收
            }
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}