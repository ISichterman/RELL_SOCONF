import java.util.Objects;

public class Carrier {
  private String Carrier;
  private String Service;

  public Carrier(String carrier, String service) {
    Carrier = carrier;
    Service = service;
  }

  public String getCarrier() {
    return Carrier;
  }

  public void setCarrier(String carrier) {
    Carrier = carrier;
  }

  public String getService() {
    return Service;
  }

  public void setService(String service) {
    Service = service;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Carrier carrier = (Carrier) o;
    return Objects.equals(getCarrier(), carrier.getCarrier()) &&
            Objects.equals(getService(), carrier.getService());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getCarrier(), getService());
  }
}
