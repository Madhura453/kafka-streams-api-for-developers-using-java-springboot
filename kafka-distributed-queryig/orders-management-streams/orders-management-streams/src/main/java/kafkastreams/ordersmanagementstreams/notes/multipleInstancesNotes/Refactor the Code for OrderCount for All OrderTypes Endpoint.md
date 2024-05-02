- http://localhost:8082/v1/orders/count

````
[
    {
        "locationId": "store_1234",
        "orderCount": 1,
        "orderType": "GENERAL"
    },
    {
        "locationId": "store_4567",
        "orderCount": 1,
        "orderType": "GENERAL"
    },
    {
        "locationId": "store_1234",
        "orderCount": 1,
        "orderType": "RESTAURANT"
    },
    {
        "locationId": "store_4567",
        "orderCount": 1,
        "orderType": "RESTAURANT"
    }
]
````

- logs
- 12:31:54.550 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - otherHosts: [HostInfoDTO[host=192.168.43.244, port=8081]]
  12:31:54.553 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.43.244, port=8081]]    
  12:31:54.561 [http-nio-8082-exec-8] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.43.244:8081/v1/orders/count/general_orders?query_other_hosts=false
  12:31:55.175 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - orderTye:general_orders,queryOtherHosts:true
  12:31:55.175 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]]
  12:31:55.177 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - otherHosts: [HostInfoDTO[host=192.168.43.244, port=8081]]
  12:31:55.177 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.43.244, port=8081]]    
  12:31:55.177 [http-nio-8082-exec-8] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.43.244:8081/v1/orders/count/restaurant_orders?query_other_hosts=false
  12:31:55.187 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - orderTye:restaurant_orders,queryOtherHosts:true
  12:31:55.188 [http-nio-8082-exec-8] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]]



http://localhost:8081/v1/orders/count

````
[
    {
        "locationId": "store_4567",
        "orderCount": 1,
        "orderType": "GENERAL"
    },
    {
        "locationId": "store_1234",
        "orderCount": 1,
        "orderType": "GENERAL"
    },
    {
        "locationId": "store_4567",
        "orderCount": 1,
        "orderType": "RESTAURANT"
    },
    {
        "locationId": "store_1234",
        "orderCount": 1,
        "orderType": "RESTAURANT"
    }
]
````

- 12:34:41.331 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - otherHosts: [HostInfoDTO[host=192.168.43.244, port=8082]]
  12:34:41.332 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.43.244, port=8082]]    
  12:34:41.340 [http-nio-8081-exec-3] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.43.244:8082/v1/orders/count/general_orders?query_other_hosts=false
  12:34:41.799 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - orderTye:general_orders,queryOtherHosts:true
  12:34:41.799 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]]
  12:34:41.800 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - otherHosts: [HostInfoDTO[host=192.168.43.244, port=8082]]
  12:34:41.800 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.43.244, port=8082]]    
  12:34:41.800 [http-nio-8081-exec-3] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.43.244:8082/v1/orders/count/restaurant_orders?query_other_hosts=false
  12:34:41.807 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - orderTye:restaurant_orders,queryOtherHosts:true
  12:34:41.807 [http-nio-8081-exec-3] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]]