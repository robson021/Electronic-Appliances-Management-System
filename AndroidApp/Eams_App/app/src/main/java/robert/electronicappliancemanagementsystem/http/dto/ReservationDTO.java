package robert.electronicappliancemanagementsystem.http.dto;

public class ReservationDTO {

    private long id;

    private String appliance;

    private String owner;

    private String where;

    private long from;

    private long minutes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppliance() {
        return appliance;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setAppliance(String appliance) {
        this.appliance = appliance;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(long minutes) {
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        return "ReservationDTO{" +
                "id=" + id +
                ", appliance='" + appliance + '\'' +
                ", owner='" + owner + '\'' +
                ", where='" + where + '\'' +
                ", from=" + from +
                ", minutes=" + minutes +
                '}';
    }
}
