alter table restaurant.restaurant_products
    add constraint restaurant_products_uniqkey
        unique (product_id, restaurant_id);