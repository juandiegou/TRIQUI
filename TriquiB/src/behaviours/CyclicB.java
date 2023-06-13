package behaviours;

import java.util.Random;

import static java.lang.Math.abs;
import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;
import models.BMachine;
import models.Board;

public class CyclicB extends CyclicBehaviour {
    private Gson gson;
    String idString;
    String address;
    private boolean firstMove = true;
    private int[][] aux;
    BMachine agent;
    private ACLMessage message;

    public CyclicB(Agent agent, String idString, String address) {
        super(agent);
        this.agent = (BMachine) agent;
        this.gson = new Gson();
        this.idString = idString;
        this.address = address;
    }

    @Override
    public void action() {

        ACLMessage response;

        try {
            this.agent.doWait(800);
            response = this.agent.blockingReceive();
            String data = response.getContent();
            this.agent.board = gson.fromJson(data, Board.class);
            //Validacion
            if(this.agent.board.checkBoard()){
                this.agent.removeBehaviour(this);
            }
            else{
            //Movimiento aleatorio nuevo
            int randomRow = new Random().nextInt(3);
            int randomCol = new Random().nextInt(3);
            //Valida que el random este entre los limites y no este marcado
            while (!this.agent.board.validateMark(randomRow, randomCol)) {
                randomRow = new Random().nextInt(3);
                randomCol = new Random().nextInt(3);
            }
            //Se marca la posición
            this.agent.board.setMark(randomRow, randomCol, 'O');
             printBoard();
            }
            
            if(!this.agent.board.checkBoard()){
                message = new ACLMessage(ACLMessage.INFORM);
                message.setContent((String) gson.toJson(this.agent.board, Board.class));
                message.addReceiver(response.getSender());
                this.agent.send(message);
            }
            else{
                this.agent.removeBehaviour(this);
            }
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * @return retorna un monimiento aleatorio con la matriz vacia.
     */
    public int[] firstMove() {
        //int valueI = new Random().nextInt(3);
        // int ValueJ = new Random().nextInt(3);
        // return new int[] {valueI,ValueJ};
        return new int[]{1, 1};
    }

    public int[] getMove() {
        //int [] bestMove={0,0}
        int[][] maxim = getMaxim();

        for (int i = 0; i < maxim.length; i++) {
            for (int j = 0; j < maxim[i].length; j++) {
                System.out.print(maxim[i][j] + " ");
            }
            System.out.println("");
        }
        // for(int candidate : maxim){
        //     validateCandidate(candidate, bestMove);
        // }
        //return bestMove;
        return new int[]{};
    }

    private int validateRow(int index) {
        int value = 0;
        for (int element : this.agent.board.cost[index]) {
            value += element;
        }
        return value;
    }

    private int validateCol(int index) {
        int value = 0;
        for (int i = 0; i < this.agent.board.cost.length; i++) {
            value += this.agent.board.cost[i][index];
        }
        return value;
    }

    private int validateDiag(int index) {
        int value = 0;
        if (index == 0) {
            for (int i = 0; i < this.agent.board.cost.length; i++) {
                value += this.agent.board.cost[i][i];
            }
        } else {
            for (int i = this.agent.board.cost.length - 1; i > 0; i--) {
                value += this.agent.board.cost[i][i];
            }
        }

        return value;
    }

    private int[][] getMaxim() {
        this.aux = this.agent.board.cost.clone();
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[i].length; j++) {
                if (aux[i][j] != 1 | aux[i][j] != 1) {
                    /**
                     *
                     *
                     * if(i==j && i<= aux.length/2){
                     * aux[i][j]=abs(validateDiag(0));
                     * }
                     * if(i==j && i>= aux.length/2){
                     * aux[i][j]=abs(validateDiag(1)); }
                     */
                    aux[i][j] += abs(validateRow(i)) + abs(validateCol(1));

                }
            }
        }
        return aux;
    }

    public void printBoard() {
        System.out.println("\n-------------------------------------------------");
        for (int[] element : this.agent.board.cost) {
            for (int value : element) {
                System.out.print("|\t" + value + "\t|");
            }
            System.out.println("\n-------------------------------------------------");
        }
    }

}
