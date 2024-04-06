package srcs.service.calculatrice;

import java.io.Serializable;

public interface Calculatrice {
    class ResDiv implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Integer q,r;

        public ResDiv(Integer a,Integer b) {
            this.q=a/b;
            this.r=a%b;
        }
        public int getQuotient() {return q;}
        public int getReste() {return r;}
    }
    
    public Integer add (Integer x, Integer y);
    public Integer sous (Integer x, Integer y);
    public Integer mult (Integer x, Integer y);
    public ResDiv div(Integer x, Integer y);
}
