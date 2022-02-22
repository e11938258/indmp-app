package at.tuwien.indmp.model.dmp;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import at.tuwien.indmp.model.DataService;
import at.tuwien.indmp.model.Entity;
import at.tuwien.indmp.service.EntityService;
import at.tuwien.indmp.util.ModelConstants;
import at.tuwien.indmp.util.Functions;

public class Distribution extends AbstractClassEntity {

    /* Properties */
    @NotNull
    private URI access_url;

    @JsonFormat(pattern = ModelConstants.DATE_FORMAT_ISO_8601)
    private Date available_until;

    private Long byte_size;

    @Pattern(regexp = ModelConstants.REGEX_DATA_ACCESS)
    private String data_access;

    private String description;

    private URI download_url;

    private List<String> format = new ArrayList<>();

    @NotNull
    private String title;

    /* Nested data structure */
    private Host host;

    private List<License> license = new ArrayList<>();

    public Distribution() {
    }

    public URI getAccess_url() {
        return this.access_url;
    }

    public void setAccess_url(URI access_url) {
        this.access_url = access_url;
    }

    public Date getAvailable_until() {
        return this.available_until;
    }

    public void setAvailable_until(Date available_until) {
        this.available_until = available_until;
    }

    public Long getByte_size() {
        return this.byte_size;
    }

    public void setByte_size(Long byte_size) {
        this.byte_size = byte_size;
    }

    public String getData_access() {
        return this.data_access;
    }

    public void setData_access(String data_access) {
        this.data_access = data_access;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getDownload_url() {
        return this.download_url;
    }

    public void setDownload_url(URI download_url) {
        this.download_url = download_url;
    }

    public List<String> getFormat() {
        return this.format;
    }

    public void setFormat(List<String> format) {
        this.format = format;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Host getHost() {
        return this.host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public List<License> getLicense() {
        return this.license;
    }

    public void setLicense(List<License> license) {
        this.license = license;
    }

    @Override
    public Object[] getValues() {
        return new Object[] {
                getData_access(),
                getAvailable_until() != null ? ModelConstants.DATE_FORMATTER_ISO_8601.format(getAvailable_until()) : null,
                getByte_size(),
                getDescription(),
                getDownload_url(),
                getFormat().toString(),
                getTitle(),
                getAccess_url().toString()
        };
    }

    @Override
    public String[] getValueNames() {
        return new String[] {
                "data_access",
                "available_until",
                "byte_size",
                "description",
                "download_url",
                "format",
                "title",
                "access_url"
        };
    }

    @Override
    public String getClassIdentifier() {
        return getAccess_url().toString();
    }

    @Override
    public List<Entity> getPropertiesFromNestedClasses(DMP dmp, String location, DataService dataService) {
        final List<Entity> properties = new ArrayList<>();

        // Host
        if (getHost() != null) {
            properties.addAll(getHost().getProperties(dmp, getLocation(location), dataService));
        }

        // License
        for (License i : getLicense()) {
            properties.addAll(i.getProperties(dmp, getLocation(location), dataService));
        }

        return properties;
    }

    @Override
    public void build(EntityService entityService, String location) {
        // Set properties
        final List<Entity> properties = entityService.findEntities(location, null);

        Entity p = Functions.findPropertyInList(getClassType(), "data_access", properties);
        setData_access(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "available_until", properties);
        try {
            setAvailable_until(p != null ? ModelConstants.DATE_FORMATTER_ISO_8601.parse(p.getValue()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        p = Functions.findPropertyInList(getClassType(), "byte_size", properties);
        setByte_size(p != null ? Long.valueOf(p.getValue()) : null);

        p = Functions.findPropertyInList(getClassType(), "description", properties);
        setDescription(p != null ? p.getValue() : null);

        p = Functions.findPropertyInList(getClassType(), "download_url", properties);
        setDownload_url(p != null ? URI.create(p.getValue()) : null);

        p = Functions.findPropertyInList(getClassType(), "format", properties);
        setFormat(p != null
                ? Arrays.asList(p.getValue().replace("[", "").replace("]", "").replace(" ", "").split(",", -1))
                : null);

        p = Functions.findPropertyInList(getClassType(), "title", properties);
        setTitle(p != null ? p.getValue() : null);

        // Set identifier
        p = Functions.findPropertyInList(getClassType(), "access_url", properties);
        setAccess_url(URI.create(p.getValue()));

        // Nested classes
        // Host
        final Entity hostIdentifier = entityService.findEntity(getLocation(location), "host:url", null);
        if (hostIdentifier != null) {
            host = new Host();
            host.build(entityService, getLocation(location) + "/" + hostIdentifier.getValue());
        }

        // License
        for (Entity property : entityService.findAllEntities(getLocation(location), "license:license_ref")) {
            final License i = new License();
            i.build(entityService, getLocation(location) + "/" + property.getValue());
            license.add(i);
        }
    }
}
