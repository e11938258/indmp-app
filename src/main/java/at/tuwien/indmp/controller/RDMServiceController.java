package at.tuwien.indmp.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import at.tuwien.indmp.model.RDMService;
import at.tuwien.indmp.modul.RDMServiceModule;
import at.tuwien.indmp.util.Endpoints;
import at.tuwien.indmp.util.ModelConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RDMServiceController {

    @Autowired
    private RDMServiceModule rdmServiceModule;

    public RDMServiceController() {
    }

    /**
     *
     * Create a new service
     *
     * @param rdmService
     */
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = Endpoints.CREATE_RDM_SERVICE, method = RequestMethod.POST)
    public void createNewRDMService(@Valid @RequestBody RDMService rdmService) {
        rdmServiceModule.persist(rdmService);
    }

    /**
     *
     * Get the RDM services
     *
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.READ_RDM_SERVICES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RDMService> readRdmServices() {
        return rdmServiceModule.getRDMServices();
    }

    /**
     *
     * UC6: Set RDM service rights
     *
     * @param accessRights
     * @param propertyRights
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = Endpoints.SET_PROPERTY_RIGHTS_TO_RDM_SERVICE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setPropertyRightsToRDMService(@PathVariable("accessRights") @NotNull @Size(min = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_MIN, max = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_MAX) @Pattern(regexp = ModelConstants.RDM_SERVICE_ACCESS_RIGHTS_REGEX) String accessRights, @Valid @RequestBody List<String> propertyRights) {
        // 2. UC8: Identify RDM service
        final RDMService rdmService = rdmServiceModule.findByAccessRights(accessRights);
        
        // 3., 3.1 The integration service updates the maDMP property rights to the specific RDM service.
        rdmService.setPropertyRights(propertyRights);
        rdmServiceModule.update(rdmService);
    }
}