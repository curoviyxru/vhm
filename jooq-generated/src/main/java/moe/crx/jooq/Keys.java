/*
 * This file is generated by jOOQ.
 */
package moe.crx.jooq;


import moe.crx.jooq.tables.Organizations;
import moe.crx.jooq.tables.Products;
import moe.crx.jooq.tables.records.OrganizationsRecord;
import moe.crx.jooq.tables.records.ProductsRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<OrganizationsRecord> ORGANIZATIONS_NAME_KEY = Internal.createUniqueKey(Organizations.ORGANIZATIONS, DSL.name("organizations_name_key"), new TableField[] { Organizations.ORGANIZATIONS.NAME }, true);
    public static final UniqueKey<OrganizationsRecord> ORGANIZATIONS_PK = Internal.createUniqueKey(Organizations.ORGANIZATIONS, DSL.name("organizations_pk"), new TableField[] { Organizations.ORGANIZATIONS.ID }, true);
    public static final UniqueKey<ProductsRecord> PRODUCTS_PK = Internal.createUniqueKey(Products.PRODUCTS, DSL.name("products_pk"), new TableField[] { Products.PRODUCTS.ID }, true);
}
