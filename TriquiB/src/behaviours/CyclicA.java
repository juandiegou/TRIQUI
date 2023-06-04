package behaviours;

import java.util.Random;

import static java.lang.Math.abs;
import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import models.AMachine;
import models.Board;

public class CyclicA extends CyclicBehaviour {

    private Gson gson;
    String idString;
    String address;
    private boolean firstMove=true;
    private int [][] aux;
    AMachine agent;

    public CyclicA(Agent agent,String idString, String address) {
        super(agent);
        this.agent = (AMachine) agent;
        this.gson = new Gson();
        this.idString= idString;
        this.address = address;
    }

    @Override
    public void action() {
        if(firstMove){
            // primera jugada
            int [] jugada = firstMove();
            while(!this.agent.board.validateMark(jugada[0], jugada[1])){
                jugada = firstMove();
            }
            this.agent.board.setMark(jugada[0], jugada[1], 'X');
            printBoard();
            
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            AID receptor = new AID(idString, AID.ISGUID); //"receptor@192.168.248.54:1099/JADE"
            receptor.addAddresses(address); //"http://192.168.254.54:7778/acc"
            msg.addReceiver(receptor);
            try {
                
                String data = gson.toJson(this.agent.board);
                msg.setContent(data);
                this.agent.send(msg);
                firstMove=false;
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        }else{
            ACLMessage response = this.agent.blockingReceive();
            try {
                String data = response.getContent();
                //this.agent.board = gson.fromJson(data, Board.class);
                printBoard();
                //calcular movimiento nuevo
                this.getMove();
                //response.setContent(this.agent.board);
                response.setContent("hola Oscarin");
                response.addReceiver(response.getSender());
                this.agent.send(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * @return retorna un monimiento aleatorio con la matriz vacia.
     */
    public int[] firstMove(){
        //int valueI = new Random().nextInt(3);
        // int ValueJ = new Random().nextInt(3);
        // return new int[] {valueI,ValueJ};
        return new int[] {1,1};
    }

    public int[] getMove(){
        //int [] bestMove={0,0}
        int [][] maxim = getMaxim();

        for(int i =0; i<maxim.length;i++){
            for(int j=0; j<maxim[i].length;j++){
                System.out.print(maxim[i][j]+" ");
            }
            System.out.println("");
        }
        // for(int candidate : maxim){
        //     validateCandidate(candidate, bestMove);
        // }
        //return bestMove;
        return new int[] {};
    }

    private void validateCandidate(int candidate, int[] best){
        int value;
        switch(candidate){
            case 0:
                value = this.agent.board.getRowSpace(0);            
                break;
            case 1:
                value = this.agent.board.getRowSpace(1);
                break;
            case 2:
                value = this.agent.board.getRowSpace(2);
                break;
            case 3:
                value = this.agent.board.getColSpace(0);
                break;
            case 4:
                value = this.agent.board.getColSpace(1);
                break;
            case 5:
                value = this.agent.board.getColSpace(2);    
                break;
            case 6:
                //
                break;
            case 7:

                break;
        }
    }

    private int validateRow(int index) {
        int value=0;
        for(int element: this.agent.board.cost[index]){
            value+=element;
        }
        return value;
    }

    private int validateCol(int index) {
        int value=0;
        for(int i=0; i<this.agent.board.cost.length;i++){
            value+=this.agent.board.cost[i][index];
        }
        return value;
    }

    private int validateDiag(int index) {
        int value=0;
        if(index==0){
            for(int i=0; i<this.agent.board.cost.length;i++){
                value+=this.agent.board.cost[i][i];
            }
        }else{
            for(int i = this.agent.board.cost.length-1;i>0; i--){
                value+=this.agent.board.cost[i][i];
            }
        }

        return value;
    }

    private int[][] getMaxim(){
        this.aux = this.agent.board.cost.clone();
        for(int i=0;i<aux.length;i++){
            for(int j=0; j<aux[i].length;j++){
                if(aux[i][j]!=1 | aux[i][j]!=1){
                    /**
                     * 
                     * 
                     if(i==j && i<= aux.length/2){
                         aux[i][j]=abs(validateDiag(0));
                     }
                     if(i==j && i>= aux.length/2){
                         aux[i][j]=abs(validateDiag(1));
                     }
                     */
                    aux[i][j]+=abs(validateRow(i))+abs(validateCol(1));  
                     
                }
            }
        }
        return aux;
    }

    public void printBoard(){
        System.out.println("\n-------------------------------------------------");
        for(int[] element: this.agent.board.cost){
            for(int value : element){
                System.out.print("|\t"+value+"\t|");
            }
            System.out.println("\n-------------------------------------------------");
        }
    }

}
