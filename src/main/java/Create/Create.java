package Create;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Create", namespace="http://schemas.microsoft.com/xrm/2011/Contracts/Services", propOrder = {
        "entity"
})
@XmlRootElement(name = "Create")
public class Create {

  @XmlElement(name = "entity", required = true)
  protected Entity entity;

  public Entity getEntity() {
    return entity;
  }

  public void setEntity(Entity entity) {
    this.entity = entity;
  }

}
