package Create;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Attributes", namespace="http://schemas.microsoft.com/xrm/2011/Contracts", propOrder = {
        "KeyValuePairOfstringanyType"
})
public class Attributes{

  @XmlElement(name = "KeyValuePairOfstringanyType", required = true, namespace="http://schemas.microsoft.com/xrm/2011/Contracts")
  protected List<KeyValuePairOfstringanyType> KeyValuePairOfstringanyType;

  public List<KeyValuePairOfstringanyType> getKeyValuePairOfstringanyType() {
    if (KeyValuePairOfstringanyType == null) {
      KeyValuePairOfstringanyType = new ArrayList<KeyValuePairOfstringanyType>();
    }
    return this.KeyValuePairOfstringanyType;
  }
}