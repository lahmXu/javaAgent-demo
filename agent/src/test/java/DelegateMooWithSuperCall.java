import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class DelegateMooWithSuperCall {

    public static String Moo1(String param1, String param2, @SuperCall Callable<String> superCall) {
        try {
            String superCallResult = superCall.call();
            return "wrapped by delegate:{" + superCallResult + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
