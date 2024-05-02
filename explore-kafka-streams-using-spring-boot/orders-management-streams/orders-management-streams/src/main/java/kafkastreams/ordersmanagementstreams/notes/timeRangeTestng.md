- Retrieve All Orders Count for  Windows by passing custom from and to time
- first http://localhost:8080/v1/orders/windows/count
````
[
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:30",
        "endWindow": "2024-01-30T04:16:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:45",
        "endWindow": "2024-01-30T04:17:00"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:30",
        "endWindow": "2024-01-30T04:17:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 3,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:45",
        "endWindow": "2024-01-30T04:18:00"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:30",
        "endWindow": "2024-01-30T04:16:45"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:45",
        "endWindow": "2024-01-30T04:17:00"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:30",
        "endWindow": "2024-01-30T04:17:45"
    },
    {
        "locationId": "store_4567",
        "orderCount": 3,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:45",
        "endWindow": "2024-01-30T04:18:00"
    },
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:30",
        "endWindow": "2024-01-30T04:16:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:45",
        "endWindow": "2024-01-30T04:17:00"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:30",
        "endWindow": "2024-01-30T04:17:45"
    },
    {
        "locationId": "store_1234",
        "orderCount": 3,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:45",
        "endWindow": "2024-01-30T04:18:00"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    }
]
````
fromTime= startWindow of any record
endtime= "endWindow" of any record
"startWindow": "2024-01-30T04:16:00",
"endWindow": "2024-01-30T04:16:15"
http://localhost:8080/v1/orders/windows/time-range/count?from_time=2024-01-30T04:16:00&to_time=2024-01-30T04:16:15

````
[
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:15",
        "endWindow": "2024-01-30T04:16:30"
    }
]
````
- So if the end time that you are passing is the start time for the next window.
 So in this case, you may be able to see the data for the window that's starting with
- If the end time window matches with the start window of the next one, then those data will also be pulled into the actual result.
- in above case to_time=2024-01-30T04:16:15 ("startWindow": "2024-01-30T04:16:15") that records also
 pulled into our result
- if you don't want result pulled into the actual result then -1 second decrease in end time

http://localhost:8080/v1/orders/windows/time-range/count?from_time=2024-01-30T04:16:00&to_time=2024-01-30T04:16:14

to_time=2024-01-30T04:16:14 so the window starts with 2024-01-30T04:16:15 not coming in result
````
[
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 9,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:16:00",
        "endWindow": "2024-01-30T04:16:15"
    }
]
````

backwardFetchAll result
http://localhost:8080/v1/orders/windows/time-range/count?from_time=2024-01-30T04:17:00&to_time=2024-01-30T04:17:15
````
[
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "GENERAL",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_1234",
        "orderCount": 14,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    },
    {
        "locationId": "store_4567",
        "orderCount": 15,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:00",
        "endWindow": "2024-01-30T04:17:15"
    },
    {
        "locationId": "store_4567",
        "orderCount": 14,
        "orderType": "RESTAURANT",
        "startWindow": "2024-01-30T04:17:15",
        "endWindow": "2024-01-30T04:17:30"
    }
]
````