package srcs.rmi.concurrent;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedVariableClassical<T extends Serializable> implements SharedVariable<T>{
    private T attr;

    private boolean verrou = false;
    private final Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    public SharedVariableClassical(T attr) {
        this.attr = attr;
    }

    @Override
    public T obtenir() throws RemoteException {
        lock.lock();
        try {
            while (verrou)
                cond.await();
            verrou = true;
            return attr;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void relacher(T x) throws RemoteException {
        lock.lock();
        try {
            verrou = false;
            attr = x;
            cond.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
