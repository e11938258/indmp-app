package at.tuwien.indmp.util;

public class Endpoints {

    private Endpoints() {
        throw new AssertionError();
    }

    // maDMP
    public static final String UPDATE_MADMP = "/madmp"; // PUT
    public static final String IDENTIFIER_CHANGE = "/madmp/instance/id"; // PUT
    public static final String DELETE_INSTANCE = "/madmp/instance"; // PUT
    public static final String GET_MADMP_IDENTIFIERS = "/madmp/identifiers"; // GET

    public static final String GET_MADMP = "/madmp"; // GET

    // System
    public static final String CREATE_NEW_RDM_SERVICE = "/system"; // POST
    public static final String UPDATE_RDM_SERVICE = "/system"; // PUT
    public static final String DELETE_RDM_SERVICE = "/system/{id}"; // DELETE
    public static final String GET_ALL_RDM_SERVICES = "/systems"; // GET
}
