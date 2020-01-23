package pro.antonshu.market.soap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import pro.antonshu.market.services.CategoryService;
import pro.antonshu.market.services.ProductService;
import pro.antonshu.market.soap.products.*;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.List;

@Endpoint
public class ProductsEndPoint {

    private static final String NAMESPACE_URI = "http://market.antonshu.pro/soap/products";

    private ProductService productService;
    private CategoryService categoryService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getProductsByCategoryRequest")
    @ResponsePayload
    public GetProductsByCategoryResponse getProducts(@RequestPayload GetProductsByCategoryRequest request) throws DatatypeConfigurationException {
        GetProductsByCategoryResponse response = new GetProductsByCategoryResponse();
        ProductsList productsList = new ProductsList();
        List<ProductDto> responseList = new ArrayList<>();
        if (categoryService.existByTitle(request.getCategory())) {
            productService.getAllProductsByCategory(categoryService.getCategoryByTitle(request.getCategory())).stream().forEach(p -> responseList.add(new ProductDto(p)));
            if (responseList.size() > 0) {
                productsList.setProductsList(responseList);
                response.setProductsList(productsList);
                response.setCategory(request.getCategory());
                response.setDescription("List of Products with needed category");
            } else {
                response.setDescription(String.format("There is no products with needed Category: %s", request.getCategory()));
            }

        } else {
            response.setDescription(String.format("This Category(%s) does not exist", request.getCategory()));
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) throws DatatypeConfigurationException {
        GetAllProductsResponse response = new GetAllProductsResponse();
        System.out.println("Doing Well with all Products");
        ProductsList productsList = new ProductsList();
        List<ProductDto> responseList = new ArrayList<>();
        productService.getAllProducts().stream().forEach(p -> responseList.add(new ProductDto(p)));
        productsList.setProductsList(responseList);
        response.setProductsList(productsList);
        response.setDescription("There is All Products in our Shop");
        return response;
    }
}
