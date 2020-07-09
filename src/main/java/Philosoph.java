import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.Random;

public class Philosoph extends Thread {
    Philosoph pLeft, pRight;
    boolean left =false, right=false;
    int rd,o;
    String who;
    Monitor m;
    Circle c;
    Label l;
    int round;
    final Object self;
    final Object onRight;
    private final Random rand = new Random();

    public Philosoph(String w, Monitor m, int rd, Circle c, Label l,Object self,Object onRight, int o) {
        this.who =w;
        this.m = m;
        this.rd = rd;
        this.c = c;
        this.l = l;
        this.round = 0;
        this.self =self;
        this.onRight = onRight;
        this.o = o;
    }
    public void assignPhilos(Philosoph l, Philosoph r) {
        this.pLeft=l;
        this.pRight=r;
    }
    public void run() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        while(rd>0) {
            if(o==0) {
                basic();
            } else if(o==1) {
                noDeadStarve();
            } else {
                noDeadNoStarve(this.pRight);
            }
        }
        Platform.runLater(() -> {
            l.setText("thinking");
            c.setStyle("-fx-fill: red");
        });
    }
    public void basic() {
        int random = (Math.abs(rand.nextInt())%100+1);
        if(random<50) {
            if (!pLeft.hasRightFork()) {
                m.waitLeftFork(this, c, l);
                m.waitRightFork(this, c, l);
            } else {
                m.waitRightFork(this, c, l);
                m.waitLeftFork(this, c, l);
            }
        } else {
            if(!pRight.hasLeftFork()) {
                m.waitRightFork(this,c,l);
                m.waitLeftFork(this,c,l);
            } else {
                m.waitLeftFork(this,c,l);
                m.waitRightFork(this,c,l);
            }
        }
        defaultStuff(1000);
        try {
            sleep(Math.abs(rand.nextInt())%1000+1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void noDeadStarve(){
        m.waitBothForks(this);
        defaultStuff(1000);
        try {
            sleep(Math.abs(rand.nextInt())%1000+1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void noDeadNoStarve(Philosoph pr){
        synchronized (onRight) {
            if ((this.who.equals("1") || this.who.equals("         4"))&&this.round==0) {
                noDeadNoStarveDo();
                round++;
            } else if(this.round==0) {
                this.round++;
                try {
                    onRight.wait();
                    noDeadNoStarveDo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    synchronized (self){
                        self.notify();
                    }
                    onRight.wait();
                    noDeadNoStarveDo();
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public void noDeadNoStarveDo() {
            m.waitBothForks(this);
            int r = Math.abs(rand.nextInt(700)+100);
            defaultStuff(r);
            try {
                sleep(800 - r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
    }
    public void defaultStuff(int duration) {
        Platform.runLater(() -> {
            l.setText("eating");
            c.setStyle("-fx-fill: greenyellow;");
        });
        System.out.println(this.who+"\u001B[32m"+" Starts eating"+"\u001B[0m");
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        m.dropForks(this, c, l);
    }
    public boolean hasLeftFork() {
        return left;
    }
    public boolean hasRightFork() {
        return right;
    }
    public boolean pickLeftFork() {
        if(!pLeft.hasRightFork()&&!this.hasLeftFork()){
            this.left=true;
            return true;
        } else {
            return false;
        }
    }
    public boolean pickRightFork() {
        if(!pRight.hasLeftFork()&&!this.hasRightFork()) {
            this.right=true;
            return true;
        } else {
            return false;
        }
    }
    public void dropLeftFork() {
        this.left=false;
    }
    public void dropRightFork() {
        this.right=false;
    }
    public boolean hasBothForks() {
        return this.hasLeftFork() && this.hasRightFork();
    }
    public void pickBothForks() {
        if(!pRight.hasLeftFork()&&!this.hasRightFork()) {
            this.right = true;
        }
        if(!pLeft.hasRightFork()&&!this.hasLeftFork()) {
            this.left = true;
        }
    }
    public void stopMe() {
        rd = 0;
        this.dropLeftFork();
        this.dropRightFork();
    }
}
