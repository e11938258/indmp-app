package at.tuwien.simulation.model.dmp;

import javax.validation.constraints.NotNull;

public abstract class Identifier extends AbstractEntity {

    @NotNull
    private String identifier;

    public abstract Object getType();

    public Identifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getClassIdentifier() {
        return this.identifier;
    }

    @Override
    public abstract String getClassType();

    @Override
    public String getLocation(String location) {
        return location;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getClassIdentifier(),
                getType(),
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "identifier",
                "type",
        };
    }

    @Override
    public String toString() {
        return "{" +
                " identifier='" + getClassIdentifier() + "'" +
                ", type='" + getType() + "'" +
                "}";
    }

}