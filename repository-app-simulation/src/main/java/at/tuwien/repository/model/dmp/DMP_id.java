package at.tuwien.repository.model.dmp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import at.tuwien.repository.util.DMPConstants;

public class DMP_id extends Identifier {

    /* Properties */
    @NotNull
    @Pattern(regexp = DMPConstants.REGEX_DATA_IDENTIFIER_TYPE)
    private String type;

    public DMP_id() {
        super(null);
    }

    public DMP_id(String identifier, String type) {
        super(identifier);
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}