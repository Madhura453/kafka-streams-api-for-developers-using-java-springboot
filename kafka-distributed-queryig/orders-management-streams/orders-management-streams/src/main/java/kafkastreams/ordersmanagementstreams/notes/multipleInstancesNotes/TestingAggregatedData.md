http://localhost:8081/v1/orders/count/general_orders

````
[
    {
        "locationId": "store_4567",
        "orderCount": 1
    },
    {
        "locationId": "store_1234",
        "orderCount": 1
    }
]
````
http://localhost:8081/v1/orders/count/restaurant_orders
````
[
{
"locationId": "store_4567",
"orderCount": 1
},
{
"locationId": "store_1234",
"orderCount": 1
}
]
````
http://localhost:8082/v1/orders/count/general_orders
````
[
    {
        "locationId": "store_1234",
        "orderCount": 1
    },
    {
        "locationId": "store_4567",
        "orderCount": 1
    }
]
````

http://localhost:8082/v1/orders/count/restaurant_orders
````
[
    {
        "locationId": "store_1234",
        "orderCount": 1
    },
    {
        "locationId": "store_4567",
        "orderCount": 1
    }
]
````

- Observe data will be same. We are getting data from all running instances
- logs
- for the 8082 port
-08:57:42.643 [http-nio-8082-exec-5] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.1.100, port=8081]]     
  08:57:42.658 [http-nio-8082-exec-5] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.1.100:8081/v1/orders/count/general_orders?query_other_hosts=false
  08:57:43.063 [http-nio-8082-exec-5] INFO  k.o.service.OrderService - orderTye:general_orders,queryOtherHosts:true
  08:57:43.063 [http-nio-8082-exec-5] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]]

- for the 8081 port
- 08:56:27.000 [http-nio-8081-exec-5] INFO  k.o.service.OrderService - otherHosts: [HostInfoDTO[host=192.168.1.100, port=8082]]
  08:56:27.000 [http-nio-8081-exec-5] INFO  k.o.service.OrderService - queryOtherHosts: true,otherHosts: [HostInfoDTO[host=192.168.1.100, port=8082]]     
  08:56:27.000 [http-nio-8081-exec-5] INFO  k.o.client.OrdersServiceClient - retrieveOrdersCountByOrderType url : http://192.168.1.100:8082/v1/orders/count/restaurant_orders?query_other_hosts=false
  08:56:27.006 [http-nio-8081-exec-5] INFO  k.o.service.OrderService - orderTye:restaurant_orders,queryOtherHosts:true
  08:56:27.006 [http-nio-8081-exec-5] INFO  k.o.service.OrderService - orderCountPerStoreDTOList :  [OrderCountPerStoreDTO[locationId=store_4567, orderCount=1]] , orderCountPerStoreDTOListOtherInstance : [OrderCountPerStoreDTO[locationId=store_1234, orderCount=1]]
