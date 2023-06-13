package models;

import behaviours.CyclicA;
import jade.core.Agent;

public class AMachine extends Agent{
    
    private int status;
    public Board board;

    protected void setup(){
        String type = (String) getArguments()[0];
        //System.out.println(type);
        board= new Board();
        addBehaviour(new CyclicA(this,"B@192.168.121.54:1099/JADE","http://192.168.121.54:7778/acc", type));
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
