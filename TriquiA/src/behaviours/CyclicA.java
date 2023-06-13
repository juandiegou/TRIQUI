package behaviours;

import java.util.Random;

import javax.swing.JOptionPane;

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
    private boolean firstMove = true;
    private int[][] aux;
    AMachine agent;
    private ACLMessage message;
    private String type;

    public CyclicA(Agent agent, String idString, String address, String type) {
        super(agent);
        this.agent = (AMachine) agent;
        this.gson = new Gson();
        this.idString = idString;
        this.address = address;
        this.type = type;
    }

    @Override
    public void action() {
        if (firstMove) {
            if(type.equals("R")){
                onMove();

            }else{
                moving();
            }
            firstMove= false;
        } else {
            ACLMessage response;
            try {
                this.agent.doWait(200);
                response = this.agent.blockingReceive();
                this.agent.board = (Board) gson.fromJson(response.getContent(), Board.class);
                printBoard();
                // calcular movimiento nuevo
                if(this.agent.board.checkBoard()){
                    this.agent.removeBehaviour(this);
                }else{
                    this.getMove();
                    this.sendMessage();
                    //this.agent.send(message);
                    printBoard2();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onMove() {
        // primera jugada
        int[] jugada = firstMove();
        while (!this.agent.board.validateMark(jugada[0], jugada[1])) {
            jugada = firstMove();
        }
        this.agent.board.setMark(jugada[0], jugada[1], 'X');
        printBoard();
        sendMessage();
    }

    private void moving() {
        String i = JOptionPane.showInputDialog(null,"Ingrese la cordenada X:");
        String j = JOptionPane.showInputDialog(null,"Ingrese la cordenada Y:");
        if(i != null && j !=null){
            try{
                int coordI = Integer.parseInt(i);
                int coordJ = Integer.parseInt(j);
                this.agent.board.setMark(coordI, coordJ, 'X');
                printBoard();
                sendMessage();
                
            }catch(NumberFormatException er){er.getStackTrace();
            }catch (Exception ex) { ex.getStackTrace();}
        }          
        
    }

    public void sendMessage(){
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            AID receptor = new AID(idString, AID.ISGUID); // "receptor@192.168.248.54:1099/JADE"
            receptor.addAddresses(address); // "http://192.168.254.54:7778/acc"
            msg.addReceiver(receptor);
            msg.setContent((String) gson.toJson(this.agent.board));
            this.agent.send(msg);
            
        } catch (Exception e) {e.getStackTrace();}
    }

    /**
     * @return retorna un monimiento aleatorio con la matriz vacia.
     */
    public int[] firstMove() {
        int valueI = new Random().nextInt(3);
        int ValueJ = new Random().nextInt(3);
        return new int[] { valueI, ValueJ };
    }

    public void getMove() {
        int[][] maxim = getMaxim();
        int valueBestMove = 0;

        for (int i = 0; i < maxim.length; i++) {
            for (int j = 0; j < maxim[i].length; j++) {
                System.out.print(maxim[i][j] + " ");
            }
            System.out.println("");
        }
        for (int i = 0; i < maxim.length; i++) {
            for (int j = 0; j < maxim[i].length; j++) {

                if (maxim[i][j] > valueBestMove) {
                    valueBestMove = maxim[i][j];
                    this.agent.board.setMark(i, j, 'X');
                }
            }
        }
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
        this.aux = getMat(this.agent.board.cost);
        for (int i = 0; i < aux.length; i++) {
            for (int j = 0; j < aux[i].length; j++) {
                if (aux[i][j] != 1 | aux[i][j] != 1) {
                    if (i == j && i <= aux.length / 2) {
                        aux[i][j] = abs(validateDiag(0));
                    }
                    if (i == j && i >= aux.length / 2) {
                        aux[i][j] = abs(validateDiag(1));
                    }
                    aux[i][j] += abs(validateRow(i)) + abs(validateCol(1));

                }
            }
        }
        return aux;
    }

    public int[][] getMat(int[][] matriz) {
        int[][] mat = new int[matriz.length][matriz[0].length];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                mat[i][j] = matriz[i][j];
            }
        }
        return mat;
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

    public void printBoard2() {
        System.out.println("\n-------------------------------------------------");
        for (char[] element : this.agent.board.game) {
            for (char value : element) {
                System.out.print("|\t" + value + "\t|");
            }
            System.out.println("\n-------------------------------------------------");
        }
    }

}
