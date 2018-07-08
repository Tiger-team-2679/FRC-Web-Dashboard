import org.team2679.dashboard.Dashboard;
import org.team2679.util.log.Logger;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[])
    {
        Logger.INSTANCE.logRobotInit();
        Logger.INSTANCE.logRobotSetup();
        Dashboard.init(2679);

        int i = 0;
        while(true){
            i += 1;
            Dashboard.putNumber("Flywheel", i);
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }
            catch (Exception c){ }
        }
    }

}
