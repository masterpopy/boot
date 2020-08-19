import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {

    @XmlElements(value = @XmlElement(name = "ValidOption"))
    public List<ValidOption> validOptions;


    public void setValidOptions(List<ValidOption> validOptions) {
        this.validOptions = validOptions;
    }

    @Override
    public String toString() {
        return validOptions.toString();
    }
}
