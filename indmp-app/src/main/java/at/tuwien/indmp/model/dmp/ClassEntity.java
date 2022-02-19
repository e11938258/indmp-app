package at.tuwien.indmp.model.dmp;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import at.tuwien.indmp.model.Permission;
import at.tuwien.indmp.model.Property;
import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.service.PropertyService;

public abstract class ClassEntity extends Entity {

    @JsonIgnore
    public abstract List<Property> getPropertiesFromIdentifier(DMP dmp, String reference, RDMService rdmService);

    public abstract void build(PropertyService propertyService, String dmpIdentifier, String classIdentifier);

    @JsonIgnore
    public List<Property> getPropertiesFromNestedClasses(DMP dmp, RDMService rdmService) {
        return new ArrayList<Property>();
    }

    @JsonIgnore
    public boolean hasRightsToUpdate(RDMService rdmService) {
        final List<Permission> permissions = rdmService.getPermissions();
        for (Permission permission : permissions) {
            if (permission.getAllowed() && permission.getClassType().equals(getClassType())) {
                return true;
            }
        }
        return false;
    }

    @JsonIgnore
    @Override
    public List<Property> getProperties(DMP dmp, String reference, RDMService rdmService) {
        // Has service rights to update the class or is it new dmp?
        if (dmp.isNew() || hasRightsToUpdate(rdmService)) {
            final List<Property> properties = super.getProperties(dmp, reference, rdmService);

            // Add identifier
            properties.addAll(getPropertiesFromIdentifier(dmp, reference, rdmService));

            return properties;
        } else {
            return new ArrayList<>();
        }
    }
}
