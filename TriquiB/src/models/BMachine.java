package models;

import behaviours.CyclicB;
import jade.core.Agent;

public class BMachine extends Agent{
    
    private int status;
    public Board board;

    @Override
    protected void setup(){
        board= new Board();
        //addBehaviour(new CyclicB(this,"receptor@192.168.113.179:1099/JADE","http://192.168.113.54:7778/acc"));
        addBehaviour(new CyclicB(this,"A@192.168.109.54:1099/JADE","http://192.168.109.54:7778/acc"));
    }


    public int getstatus(){
        return this.status;
    }

    public void setStatus(int status){
        if(status>=0 && status<=1){
            this.status = status;
        }
    }
}
