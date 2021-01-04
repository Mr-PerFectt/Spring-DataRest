package com.mrperfect.ecom.config;

import com.mrperfect.ecom.entity.Product;
import com.mrperfect.ecom.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    @Autowired
   private EntityManager entityManager;




    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

        HttpMethod [] theUnsupportedActions ={HttpMethod.PUT,HttpMethod.POST,HttpMethod.DELETE};

        //Disabling Http Method for Products

        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));


        //Disabling Http Method for Product-Category

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        //calling internal helper method
        exposeIds(config);

    }
    private void exposeIds(RepositoryRestConfiguration  config){
        // expose entity id

        //get list of all entity classes from entity manager

        Set<EntityType<?>> entities =entityManager.getMetamodel().getEntities();

        //create array of entity types

        List<Class> entityClasses = new ArrayList<>();

        // get entity types for the entities

        for(EntityType tempEntityType :entities){

            entityClasses.add(tempEntityType.getJavaType());
        }

        // Expose the Entity id for the array of entity domain type

        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }

}
