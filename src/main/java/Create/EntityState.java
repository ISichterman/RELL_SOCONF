package Create;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityState", namespace="http://schemas.microsoft.com/xrm/2011/Contracts", propOrder = {
        "value"
})
public class EntityState {
  @XmlAttribute(name = "xsi:nil")
  protected String nil = "true";

  @XmlValue
  protected String value;

  public String getNil() {
    return nil;
  }

  public void setNil(String nil) {
    this.nil = nil;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}
