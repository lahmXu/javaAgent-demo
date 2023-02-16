import net.bytebuddy.implementation.bind.annotation.Super;

public class DelegateMooWithSuper {

    public static String Moo1(String param1, @Super Moo uper) {
        System.out.println("invoke time: " + System.currentTimeMillis());
        return uper.Moo1(param1);
    }

}
