package moe.crx.api;

import com.google.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Feature;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import moe.crx.dao.OrganizationDao;
import moe.crx.dao.ProductDao;
import moe.crx.dto.Organization;
import moe.crx.dto.Product;
import org.jetbrains.annotations.NotNull;

import static moe.crx.api.Responser.drop;
import static moe.crx.api.Responser.dropOk;

@Path("/products")
@Singleton
public final class ProductManageREST implements Feature {

    private final OrganizationDao organizationDao;
    private final ProductDao productDao;

    @Inject
    public ProductManageREST(@NotNull OrganizationDao organizationDao,
                             @NotNull ProductDao productDao) {
        this.organizationDao = organizationDao;
        this.productDao = productDao;
    }

    @Path("/delete")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response productsDelete(@FormParam("name") String name) {
        if (name.trim().isBlank())
            return drop("Product name is empty.");

        var product = productDao.readByName(name);

        if (product == null)
            return drop(Response.Status.NOT_FOUND, "Product was not found.");

        if (!productDao.delete(product))
            return drop(Response.Status.INTERNAL_SERVER_ERROR, "Internal error: product deletion.");

        return dropOk("Product has been deleted.");
    }

    @Path("/add")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response productsAdd(@FormParam("name") String productName,
                                @FormParam("manufacturer") String organizationName,
                                @FormParam("amount") Integer amount) {
        if (productName.trim().isBlank())
            return drop("Product name is empty.");

        if (organizationName.trim().isBlank())
            return drop("Manufacturer name is empty.");

        if (amount < 0)
            return drop("Amount can't be a negative number.");

        var org = organizationDao.create(new Organization(0, organizationName));

        if (org == null)
            return drop(Response.Status.INTERNAL_SERVER_ERROR, "Internal error: org is null.");

        var product = productDao.create(new Product(0, productName, org.getId(), amount));

        if (product == null)
            return drop(Response.Status.INTERNAL_SERVER_ERROR, "Internal error: product is null.");

        return dropOk("Product has been added.");
    }

    @Override
    public boolean configure(FeatureContext context) {
        return true;
    }
}
