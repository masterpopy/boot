import org.junit.Test;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Run {

    @Test
    public void run1() throws Throwable {
        System.out.println("255".compareTo("254"));
    }


    @Test
    public void runResponse() {
        System.out.println("\u670D\u52A1");
    }

    private class domains {
        type[] types;

    }

    private class type {
        private String[] attr;
    }

    public void addDefination(String def) {
        int lp = def.indexOf('(');
        int rp = def.indexOf(')');
        int dot = def.indexOf('.', lp);
        String name = def.substring(0, lp);
        lp += 1;
        int category;
        int domainId;
        if (dot == -1) {
            category = -1;
            domainId = Integer.parseInt(def.substring(lp, rp));
        } else {
            domainId = Integer.parseInt(def.substring(lp, dot));
            category = Integer.parseInt(def.substring(dot + 1, rp));
        }
        System.out.println(name + category + domainId);
        System.out.println(category);
        System.out.println(domainId);
    }

    @Test
    public void testDomain() throws Exception {
        JMXServiceURL jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7200/jmxrmi");
        Map<String, String[]> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, new String[]{"thanos", "thanos123.com"});
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);
        MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
        System.out.println(mbsc.queryMBeans(new ObjectName("config:parent=/Tongweb/Server,name=Web*"), null));
    }

    @Test
    public void testJMX() throws Exception {
        JMXServiceURL jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:7200/jmxrmi");
        Map<String, String[]> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, new String[]{"thanos", "thanos123.com"});
        JMXConnector jmxConnector = JMXConnectorFactory.connect(jmxUrl, env);
        MBeanServerConnection mbsc = jmxConnector.getMBeanServerConnection();
        String[] domains = mbsc.getDomains();
        Arrays.sort(domains);
        int domainCnt = 1;
        int objCnt = 1;
        int attrCnt = 1;
        for (String domain : domains) {
            System.out.print(domain);
            System.out.println("(" + domainCnt++ + ")");
            ObjectName[] objectNames = mbsc.queryNames(new ObjectName(domain + ":*"), null).toArray(new ObjectName[0]);
            Arrays.sort(objectNames);
            for (ObjectName objectName : objectNames) {
                System.out.print(" ");
                System.out.print(objectName.toString());
                System.out.println("(" + objCnt++ + ")");
                MBeanAttributeInfo[] mBeanInfo = mbsc.getMBeanInfo(objectName).getAttributes();
                Arrays.sort(mBeanInfo, Comparator.comparing(MBeanFeatureInfo::getName));
                for (MBeanAttributeInfo attribute : mBeanInfo) {
                    System.out.print("  ");
                    System.out.print(attribute.getName());
                    System.out.println("(" + attrCnt++ + ")");
                }
                attrCnt = 1;
            }
            objCnt = 1;
        }
        jmxConnector.close();
    }
}
