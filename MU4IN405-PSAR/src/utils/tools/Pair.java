package utils.tools;

import java.io.Serializable;
import java.util.Objects;

public class Pair implements Serializable {
    private Object o1;
    private Object o2;

    public Pair(Object o1, Object o2){
        this.o1 = o1;
        this.o2 = o2;
    }

    public Object first(){
        return o1;
    }

    public Object second(){
        return o2;
    }

    @Override
    public String toString() {
        return "[" + o1.toString() + ", " + o2.toString() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair pair = (Pair) o;
        return Objects.equals(o1, pair.first()) && Objects.equals(o2, pair.second());
    }

    @Override
    public int hashCode() {
        return Objects.hash(o1, o2);
    }
}
