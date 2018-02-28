package com.ryanfranklin.myretail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Product {

    @NotNull
    @Size(min=1)
    private String id;
    @NotNull
    @Size(min=1)
    private String name;
    @Valid
    @NotNull
    private CurrentPrice currentPrice;

    /** Jackson constructor **/
    public Product(){}

    /**
     * Constructs a Product.
     * @param id the unique id of this product, not null
     * @param name the name of this product, not null
     * @param currentPrice the current price of this product, not null
     */
    public Product(String id, String name, CurrentPrice currentPrice) {
        //TODO: Validate not null
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    /**
     * Gets id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id
     *
     * @param id the of id of the Product
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name
     *
     * @param name the of name of the Product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets currentPrice
     *
     * @return the currentPrice
     */
    public CurrentPrice getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Sets currentPrice
     *
     * @param currentPrice the of currentPrice of the Product
     */
    public void setCurrentPrice(CurrentPrice currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(currentPrice, product.currentPrice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, currentPrice);
    }

    @Document
    public static class CurrentPrice {

        @Id
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String id;
        @NotNull
        @Size(min=1)
        private String value;
        @NotNull
        @Size(min=1)
        private String currencyCode;

        /**
         * Gets id
         *
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Gets value
         *
         * @return the value
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets value
         *
         * @param value the of value of the CurrentPrice
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets currencyCode
         *
         * @return the currencyCode
         */
        public String getCurrencyCode() {
            return currencyCode;
        }

        /**
         * Sets currencyCode
         *
         * @param currencyCode the of currencyCode of the CurrentPrice
         */
        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CurrentPrice that = (CurrentPrice) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(value, that.value) &&
                    Objects.equals(currencyCode, that.currencyCode);
        }

        @Override
        public int hashCode() {

            return Objects.hash(id, value, currencyCode);
        }
    }
}
