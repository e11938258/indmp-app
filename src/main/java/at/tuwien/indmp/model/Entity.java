package at.tuwien.indmp.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.util.ModelConstants;

/**
 * 
 * https://www.w3.org/TR/2013/REC-prov-o-20130430/#Entity
 * 
 */
@javax.persistence.Entity
@Table(name = "entity")
public class Entity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false, precision = 18, scale = 0)
    @JsonIgnore
    private Long id; // Just database identifier

    @Column(name = "at_location", nullable = false)
    @Size(min = ModelConstants.ENTITY_AT_LOCATION_MIN, max = ModelConstants.ENTITY_AT_LOCATION_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_AT_LOCATION_REGEX)
    private String atLocation; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#atLocation

    @Column(name = "specialization_of", nullable = false)
    @Size(min = ModelConstants.ENTITY_SPECIALIZATION_OF_MIN, max = ModelConstants.ENTITY_SPECIALIZATION_OF_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_SPECIALIZATION_OF_REGEX)
    private String specializationOf; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#specializationOf

    @Column(nullable = false)
    @Size(min = ModelConstants.ENTITY_VALUE_MIN, max = ModelConstants.ENTITY_VALUE_MAX)
    @Pattern(regexp = ModelConstants.ENTITY_VALUE_REGEX)
    private String value; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#value

    @OneToOne
    @JoinColumn(name = "was_generated_by", referencedColumnName = "id")
    private Activity wasGeneratedBy; // https://www.w3.org/TR/2013/REC-prov-o-20130430/#wasGeneratedBy

    public Entity() {
    }

    public Entity(String atLocation, String specializationOf, String value, Activity wasGeneratedBy) {
        this.atLocation = atLocation;
        this.specializationOf = specializationOf;
        this.value = value;
        this.wasGeneratedBy = wasGeneratedBy;
    }

    @JsonIgnore
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public String getSpecializationOf() {
        return this.specializationOf;
    }

    public void setSpecializationOf(String specializationOf) {
        this.specializationOf = specializationOf;
    }

    public String getAtLocation() {
        return this.atLocation;
    }

    public void setAtLocation(String atLocation) {
        this.atLocation = atLocation;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Activity getWasGeneratedBy() {
        return this.wasGeneratedBy;
    }

    public void setWasGeneratedBy(Activity wasGeneratedBy) {
        this.wasGeneratedBy = wasGeneratedBy;
    }

    public boolean hasSameValue(Entity entity) {
        return getValue().equals(entity.getValue());
    }

    public boolean hasSameService(DataService dataService) {
        return getWasGeneratedBy().getWasAssociatedWith().equals(dataService);
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", atLocation='" + getAtLocation() + "'" +
                ", specializationOf='" + getSpecializationOf() + "'" +
                ", value='" + getValue() + "'" +
                ", wasGeneratedBy='" + getWasGeneratedBy() + "'" +
                "}";
    }
}
