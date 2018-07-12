import org.team2679.dashboard.Dashboard;
import org.team2679.logging.Logger;

public class Main {
    public static void main(String args[]){
        Logger.INSTANCE.init("/home/slowl0ris/FRC", "RobotLog");
        Dashboard.INSTANCE.init(3000);
        Dashboard.INSTANCE.registerView(new Index());
        Logger.INSTANCE.init();
    }
}
