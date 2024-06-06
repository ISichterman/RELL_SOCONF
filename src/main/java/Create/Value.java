package Create;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "value", namespace="http://schemas.microsoft.com/xrm/2011/Contracts", propOrder = {
        "value"
})
public class Value {
  @XmlAttribute(name = "type", namespace="http://www.w3.org/2001/XMLSchema-instance")
  protected String type;

  @XmlAttribute(name = "xmlns:xs")
  protected String xlms = "http://www.w3.org/2001/XMLSchema";

  @XmlValue
  protected String value;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getXlms() {
    return xlms;
  }
}
