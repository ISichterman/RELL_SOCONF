package Create;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KeyValuePairOfstringanyType", namespace="http://schemas.microsoft.com/xrm/2011/Contracts", propOrder = {
        "key",
        "value"
})
public class KeyValuePairOfstringanyType{

  @XmlElement(name = "key", required = true, namespace="http://schemas.datacontract.org/2004/07/System.Collections.Generic")
  protected String key;

  @XmlElement(name = "value", required = true, namespace="http://schemas.datacontract.org/2004/07/System.Collections.Generic")
  protected Value value;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }

}