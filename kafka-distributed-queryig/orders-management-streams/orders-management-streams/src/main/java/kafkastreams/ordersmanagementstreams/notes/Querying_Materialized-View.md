- until now what we have been doing, we have been validating the data by actually scanning through
  the logs and there is no way for any other external team or external partners who would like to look
  at the data to access the data still sitting inside the app at this point.
  So the goal of this section is to how to access the data and how to expose the data to the outside world
- aggregated data will be there in rocks db, and we need to expose through restful API.
- restful API that can interact with the state store, which is rocksdb,
  and then get the data out of it and expose the data to the outside world using a rest call
-  state stores=materialized views where our aggregated result stored
- going to be building Rest API that's going to interact with each of these state store
- we are materializing the data as a store name
- The store name that's been passed over here is basically the general orders count
````
 public static final String GENERAL_ORDERS_COUNT_WINDOWS_STORE = "general_orders_count_window";
    public static final String GENERAL_ORDERS_REVENUE_WINDOWS_STORE = "general_orders_revenue_window";
    public static final String GENERAL_ORDERS_COUNT = "general_orders_count";
    public static final String GENERAL_ORDERS_REVENUE = "general_orders_revenue";

    public static final String RESTAURANT_ORDERS_COUNT = "restaurant_orders_count";
    public static final String RESTAURANT_ORDERS_COUNT_WINDOWS_STORE = "restaurant_orders_count_window";
    public static final String RESTAURANT_ORDERS_REVENUE_WINDOWS_STORE = "restaurant_orders_revenue_window";
    public static final String RESTAURANT_ORDERS_REVENUE = "restaurant_orders_revenue";

````
The above are store names

- So the beauty of this one is that once you have this data exposed as a rest endpoint, you can have 
- any number of clients access to this data and build functionalities based on this data