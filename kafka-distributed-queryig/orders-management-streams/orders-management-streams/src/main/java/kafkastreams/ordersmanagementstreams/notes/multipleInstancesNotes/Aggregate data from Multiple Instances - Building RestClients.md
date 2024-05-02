````
  @RequestParam(value="query_other_hosts", required=false) String queryOtherHosts
````
- make sure the other instance is not going to make any network call to other instances.
   Let's say if you make a call to this function retrieveDataFromOtherInstances(orderType).
- So the call will be made to the other instance, and the other instance is also going to hold the same
logic. So in this case, it's going to make the call going in circles.
So we want to stop that behavior.

## So how do we stop that behavior?
````
@RequestParam(value="query_other_hosts", required=false)
````
- this request parameter is going to instruct the other clients whether to make a call or not.
- If the value is true, then go ahead and make the call.
- If the value is false, then do not make the call.
- So the reason for this code is to make sure when the other instances receive the request, we don't 
- want any network call to be made from that particular instance.And way we control it is by using this property
````
.queryParam("query_other_hosts",false)
````
- this is going to make sure and instruct the other instance when it receives this request.
  Do not make the call to the other instances
- So this is how we are going to solve the problem of making the other instances, making another network
call on it, and it will go in circles and we want to avoid that