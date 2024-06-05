package com.cody.backend.full.cache.batch.constant;

public class Query {
    public static String getDisplayProductQuery() {
        return "select b.id as brand_id, c.id as category_id,\n"
            + " p.id as product_id, c.name as category_name, p.name as product_name,\n"
            + " b.name as brand_name, price as product_price\n"
            + " from product p join brand b on \n"
            + "brand_id = b.id join category c on c.id = category_id";
    }
    public static String getAllUserQuery() {
        return "select s.id as seller_id, u.id as user_id, u.name as user_name, s.name as seller_name from seller s join users u on s.id = u.id";
    }
}
