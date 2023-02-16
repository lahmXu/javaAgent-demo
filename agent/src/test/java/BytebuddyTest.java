import com.lahmxu.HelloWorld;
import com.lahmxu.agent.SpringContextRegisterListenerInit;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class BytebuddyTest {


    @Test
    public void test_redefine() throws Exception {
        Object helloWorld = new ByteBuddy()
                .redefine(HelloWorld.class)
                .make()
                .saveIn(new File("/Users/lahmxu/Documents/workspace/java/javaAgent-demo/agent/src/test/java/"));
    }

    @Test
    public void test_create_method() throws Exception {
        Object helloWorld = new ByteBuddy()
                .subclass(Object.class)
                .name("com.lahmxu")
                .defineMethod("helloWorld", String.class, Modifier.PUBLIC)
                .intercept(FixedValue.value("hello world22"))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded()
                .newInstance();

        Method method = helloWorld.getClass().getMethod("helloWorld");
        System.out.println(method.invoke(helloWorld));;
    }

    @Test
    public void test_delegateMoo() throws Exception {
        Moo moo = new ByteBuddy()
                .subclass(Moo.class)
                .method(ElementMatchers.named("Moo").or(ElementMatchers.named("Moo1")))
                .intercept(MethodDelegation.to(DelegateMoo.class))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded()
                .newInstance();

        System.out.println("moo: " + moo.Moo("lahmxu", "31"));
        System.out.println("moo1: " + moo.Moo1("lahmxu"));
    }

    @Test
    public void test_super() throws Exception {
        Moo moo = new ByteBuddy()
                .subclass(Moo.class)
                .method(ElementMatchers.named("Moo").or(ElementMatchers.named("Moo1")))
                .intercept(MethodDelegation.to(DelegateMooWithSuper.class))
                .make()
                .load(ClassLoader.getSystemClassLoader())
                .getLoaded()
                .newInstance();
        System.out.println("moo1: " + moo.Moo1("lahmxu"));
    }

    @Test
    public void test_superCall() throws Exception {

    }

    @Test
    public void test_superCall11() throws Exception {

    }
}
