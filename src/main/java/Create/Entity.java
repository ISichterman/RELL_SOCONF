package Create;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entity", namespace="http://schemas.microsoft.com/xrm/2011/Contracts/Services", propOrder = {
        "Attributes",
        "EntityState",
        "FormattedValues",
        "Id",
        "LogicalName",
        "RelatedEntities"
})
public class Entity {

  @XmlElement(name = "Attributes", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected Attributes Attributes;

  @XmlElement(name = "EntityState", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected EntityState EntityState = new EntityState();

  @XmlElement(name = "FormattedValues", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected FormattedValues FormattedValues = new FormattedValues();

  @XmlElement(name = "Id", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected String Id;

  @XmlElement(name = "LogicalName", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected String LogicalName;

  @XmlElement(name = "RelatedEntities", required = true, namespace = "http://schemas.microsoft.com/xrm/2011/Contracts")
  protected RelatedEntities RelatedEntities = new RelatedEntities();

  public Attributes getAttributes() {
    return Attributes;
  }

  public void setAttributes(Attributes attributes) {
    Attributes = attributes;
  }

  public String getId() {
    return Id;
  }

  public void setId(String id) {
    Id = id;
  }

  public String getLogicalName() {
    return LogicalName;
  }

  public void setLogicalName(String logicalName) {
    LogicalName = logicalName;
  }

  public EntityState getEntityState() {
    return EntityState;
  }

  public FormattedValues getFormattedValues() {
    return FormattedValues;
  }

  public RelatedEntities getRelatedEntities() {
    return RelatedEntities;
  }
}