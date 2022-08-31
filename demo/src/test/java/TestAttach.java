import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class TestAttach {

    public static void main(String[] args) throws Exception {
        //获取当前系统中所有 运行中的 虚拟机
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            //如果虚拟机的名称为 xxx 则 该虚拟机为目标虚拟机，获取该虚拟机的 pid
            //然后加载 agent.jar 发送给该虚拟机
            System.out.println(vmd.displayName());
            if (vmd.displayName().endsWith("com.lahmxu.demo.DemoApplication")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("/Users/lahmxu/Documents/workspace/java/javaAgent-demo/agent/target/agent-0.0.1-SNAPSHOT.jar","com.lahmxu.demo.TestUtil&hello");
                Thread.sleep(1000L);
                virtualMachine.detach();
            }
        }
    }
}
