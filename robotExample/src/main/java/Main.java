import org.team2679.dashboard.Dashboard;
import org.team2679.util.log.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String args[])
    {
        Logger.INSTANCE.logRobotInit();
        Logger.INSTANCE.logRobotSetup();
        Logger.INSTANCE.init("/home/slowl0ris/FRC");
        Logger.INSTANCE.logThrowException(new Exception("this is supposed to be a very very very long exception, cool and good"));
        Dashboard.INSTANCE.init(2679);

        int i = 0;
        while(true){
            i += 1;
            Dashboard.INSTANCE.putNumber("Flywheel", i);
            if(i%100 == 0){
                Logger.INSTANCE.logWarning("hundred boom!");
            }
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            }
            catch (Exception c){ }
        }
    }
}
