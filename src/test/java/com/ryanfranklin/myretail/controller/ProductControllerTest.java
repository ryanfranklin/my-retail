package com.ryanfranklin.myretail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryanfranklin.myretail.exception.NotFoundException;
import com.ryanfranklin.myretail.model.Product;
import com.ryanfranklin.myretail.repository.ProductImplRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 *
 *
 {
     "id": "16696652",
     "name": "Beats Solo 2 Wireless - Black",
     "currentPrice": {
     "value": "50:50",
     "currencyCode": "USD"
     }
 }
 */
//@RunWith(SpringRunner.class)
//@WebMvcTest(ProductController.class)
@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    private static String URL_PATH = "/products/";
    private static String JSON_TAG_ID = "id";
    private static String JSON_TAG_NAME = "name";
    private static String JSON_TAG_CURRENT_PRICE = "currentPrice";
    private static String JSON_TAG_VALUE = "value";
    private static String JSON_TAG_CURRENCY_CODE = "currencyCode";
    private static String JSON_PATH_VALUE = "currentPrice.value";
    private static String JSON_PATH_CURRENCY_CODE = "currentPrice.currencyCode";

    private static String PRODUCT_ID = "123456";
    private static String PRODUCT_NAME = "Beats Solo 2 Wireless - Black";
    private static String PRICE_VALUE = "50:50";
    private static String PRICE_CURRENCY_CODE = "USD";

    private MockMvc mockMvc;

    @Mock
    private ProductImplRepository productImplRepository;

    @InjectMocks
    private ProductController productController;

    // This object will be magically initialized by the initFields method below.
    private JacksonTester<Product> jacksonProductTester;
    private Product productGood;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new ProductControllerExceptionHandler())
                .build();

        Product.CurrentPrice currentPrice = new Product.CurrentPrice();
        currentPrice.setValue(PRICE_VALUE);
        currentPrice.setCurrencyCode(PRICE_CURRENCY_CODE);
        productGood = new Product();
        productGood.setId(PRODUCT_ID);
        productGood.setName(PRODUCT_NAME);
        productGood.setCurrentPrice(currentPrice);
    }


    @Test
    public void getProductById() throws Exception {

        given(productImplRepository.findOne(productGood.getId())).willReturn(productGood);

        MockHttpServletResponse response = mockMvc.perform(get(URL_PATH + productGood.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jacksonProductTester.write(productGood).getJson());
    }

    @Test
    public void getProductByIdNotFound() throws Exception {

        given(productImplRepository.findOne(productGood.getId())).willThrow(new NotFoundException());

        MockHttpServletResponse response = mockMvc.perform(get(URL_PATH + productGood.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void getProductByIdMethodNotSupported() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(patch(URL_PATH + productGood.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void updateProductById() throws Exception {

        MockHttpServletResponse response = mockMvc.perform(put(URL_PATH + productGood.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProductTester.write(productGood).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void updateProductByIdNotFound() throws Exception {

        doThrow(new NotFoundException()).when(productImplRepository).update(productGood.getId(), productGood);

        MockHttpServletResponse response = mockMvc.perform(put(URL_PATH + productGood.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProductTester.write(productGood).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getContentAsString()).isEmpty();
    }

    @Test
    public void updateProductByIdBadRequest() throws Exception {

        Product.CurrentPrice currentPrice = new Product.CurrentPrice();
        currentPrice.setValue(PRICE_VALUE);
        currentPrice.setCurrencyCode(PRICE_CURRENCY_CODE);
        Product productBadJson = new Product();
        productBadJson.setId(PRODUCT_ID);
        productBadJson.setCurrentPrice(currentPrice);

        MockHttpServletResponse response = mockMvc.perform(put(URL_PATH + productBadJson.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonProductTester.write(productBadJson).getJson()))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEmpty();
    }
}