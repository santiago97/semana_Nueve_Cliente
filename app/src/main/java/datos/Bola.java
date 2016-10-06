package datos;

import java.io.Serializable;

/**
 * Created by Santiago on 03/10/2016.
 */
public class Bola implements Serializable {

    private static final long serialVersionUID = 1L;
    int x,y,r,g,b,tam;

    public Bola(int x, int y, int tam, int r, int g , int  b ){
        this.x=x;
        this.y=y;
        this.r=r;
        this.g=g;
        this.b=b;
        this.tam=tam;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public int getTam() {
        return tam;
    }



}