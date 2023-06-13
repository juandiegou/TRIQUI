import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;


public class Main {

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {

        Runtime runtime = Runtime.instance();
        
        Profile profile = new ProfileImpl("localhost",1099,"Main-Container",true);    
        try {
            System.out.println(profile);
            AgentController agentController = runtime.createMainContainer(profile).createNewAgent("A", "models.AMachine",new Object[]{"A"} );
            agentController.start();
            System.out.println("agentController");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    
}
