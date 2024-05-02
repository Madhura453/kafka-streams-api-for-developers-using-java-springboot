- query the Windows State stores, because until now, we have
  learned about how to query the key value state stores
- by default, the windowed data is of type GMT time zone
````
[
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:00",
        "endWindow": "2024-01-29T13:38:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:15",
        "endWindow": "2024-01-29T13:38:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:30",
        "endWindow": "2024-01-29T13:38:45"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:45",
        "endWindow": "2024-01-29T13:39:00"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:00",
        "endWindow": "2024-01-29T13:39:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:15",
        "endWindow": "2024-01-29T13:39:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:30",
        "endWindow": "2024-01-29T13:39:45"
    },
    {
        "locationId": "store_4567",
        "orderCount": 4,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:45",
        "endWindow": "2024-01-29T13:40:00"
    },
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:00",
        "endWindow": "2024-01-29T13:38:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:15",
        "endWindow": "2024-01-29T13:38:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:30",
        "endWindow": "2024-01-29T13:38:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:38:45",
        "endWindow": "2024-01-29T13:39:00"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:00",
        "endWindow": "2024-01-29T13:39:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:15",
        "endWindow": "2024-01-29T13:39:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:30",
        "endWindow": "2024-01-29T13:39:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 4,
        "orderType": "GENERAL",
        "startWindow": "2024-01-29T13:39:45",
        "endWindow": "2024-01-29T13:40:00"
    }
]
````

- we are going to implement an interesting use case where
the endpoint is going to take the from time and to time as an input argument and it's going to query
the windowed state store and pull the data that matches this from time end to time
- I want to know within an hour how many orders have been received or how many restaurant orders or how
many general orders
````
 http://localhost:8080/v1/orders/windows/count?from_time=2023-02-16T11:27:00&to_time=2023-02-16T11:27:00
 ````
- it's going to be the standard ISO date time format that's going to be sent as part
of the request

# backwardFetchAll
- this is going to do the fetch in the descending order fashion, which means the latest window is
going to be the first result and the earliest window is going to be the last result.