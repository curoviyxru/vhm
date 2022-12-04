package moe.crx.api;

import com.google.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moe.crx.dto.OrganizationProductList;
import moe.crx.dto.ProductList;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.api.generators.ProductListContentGenerator;
import org.jetbrains.annotations.NotNull;

import static moe.crx.api.Responser.drop;
import static moe.crx.api.Responser.dropOk;

@Path("/products")
@Singleton
public final class ProductListREST implements Feature {

    private final OrganizationDao organizationDao;
    private final ProductDao productDao;
    private final ProductListContentGenerator contentGenerator;

    @Inject
    public ProductListREST(@NotNull OrganizationDao organizationDao,
                           @NotNull ProductDao productDao,
                           @NotNull ProductListContentGenerator contentGenerator) {
        this.organizationDao = organizationDao;
        this.productDao = productDao;
        this.contentGenerator = contentGenerator;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response productsPage() {
        return dropOk(contentGenerator.writeContent(productDao.all(), organizationDao.all()));
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response productsList() {
        return dropOk(new ProductList(productDao.all(), organizationDao.all()));
    }

    @Path("/{name}/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response productsList(@PathParam("name") String name) {
        if (name.trim().isBlank())
            return drop("Manufacturer name is empty.");

        var organization = organizationDao.readByName(name);

        if (organization == null)
            return drop(Response.Status.NOT_FOUND, "Manufacturer was not found.");

        return dropOk(new OrganizationProductList(productDao.allByOrgId(organization.getId())));
    }

    @Override
    public boolean configure(FeatureContext context) {
        return true;
    }
}
