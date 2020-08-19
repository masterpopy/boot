import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ValidOption")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidOption {
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String explain;




    public void setName(String name) {
        this.name = name;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "ValidOption{" +
                "name='" + name + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
