package com.demoblaze.data;

import com.demoblaze.model.PurchaseData;

public class TestData {

    public static class Credentials {
        public static final String VALID_USERNAME = "admin";
        public static final String VALID_PASSWORD = "admin";
        public static final String INVALID_USERNAME = "wronguser";
        public static final String INVALID_PASSWORD = "wrongpass";
    }

    public static class Categories {
        public static final String[] EXPECTED_CATEGORIES = {"Phones", "Laptops", "Monitors"};
        public static final String PHONES = "Phones";
        public static final String LAPTOPS = "Laptops";
        public static final String MONITORS = "Monitors";
    }

    public static class Products {
        public static final String SAMSUNG_GALAXY_S6 = "Samsung galaxy s6";
        public static final String NEXUS_6 = "Nexus 6";
        public static final String IPHONE_6_32GB = "Iphone 6 32gb";
        public static final String SONY_VAIO_I5 = "Sony vaio i5";
        public static final String MACBOOK_AIR = "MacBook air";
    }

    public static PurchaseData getDefaultPurchaseData() {
        return new PurchaseData(
                "Diego QA",
                "Colombia",
                "Bogotá",
                "4111111111111111",
                "12",
                "2030"
        );
    }

    public static PurchaseData getIncompletePurchaseData() {
        return new PurchaseData(
                "",  // Empty name
                "Colombia",
                "Bogotá",
                "4111111111111111",
                "12",
                "2030"
        );
    }

    public static class ProfileData {
        public static final String INITIAL_FULLNAME = "Admin User";
        public static final String INITIAL_EMAIL = "admin@example.com";
        public static final String UPDATED_FULLNAME = "Admin User Updated";
        public static final String UPDATED_EMAIL = "admin.updated@example.com";
        public static final String PROFILE_KEY = "dmz_profile";
    }

    public static class URLs {
        public static final String BASE_URL = "https://www.demoblaze.com/";
        public static final String CART_URL = BASE_URL + "cart.html";
        public static final String INDEX_URL = BASE_URL + "index.html";
    }

    public static class Messages {
        public static final String PRODUCT_ADDED = "Product added";
        public static final String USER_DOES_NOT_EXIST = "User does not exist";
        public static final String WRONG_PASSWORD = "Wrong password";
        public static final String ORDER_CONFIRMATION_PATTERN = "Id: \\d+";
    }
}