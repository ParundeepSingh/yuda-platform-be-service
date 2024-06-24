## Endpoint Details

Have exposed a single endpoint which takes the `video_id` and provides the <b> View Count Trend</b>.

<br>

```
Endpoint: /rest/video/view-count-trend?videoId=<<VIDEO_ID>>
```

### CURL:

```
curl --location 'http://localhost:8080/rest/video/view-count-trend?videoId=<<VIDEO_ID>>'

```
Please replace <<VIDEO_ID>> by actual video_id whose data is ingested in our system.


### Sample response:
This Response will hold the viewCount with timestamp for every hour.
```
{
    "videoId": "8nV6IvnalZc",
    "viewCount": [
        {
            "timestamp": "2024-06-25T01:00:47Z",
            "viewCount": 8986998
        },
        {
            "timestamp": "2024-06-25T01:00:42Z",
            "viewCount": 8986998
        },
        {
            "timestamp": "2024-06-25T00:51:39Z",
            "viewCount": 8986982
        },
        {
            "timestamp": "2024-06-25T00:44:14Z",
            "viewCount": 8986965
        },
        {
            "timestamp": "2024-06-24T23:41:40Z",
            "viewCount": 8986810
        },
        {
            "timestamp": "2024-06-24T23:10:57Z",
            "viewCount": 8986797
        }
    ]
}
```

### Screenshot:
<img width="1039" alt="Screenshot 2024-06-25 at 1 52 25 AM" src="https://github.com/ParundeepSingh/yuda-platform-be-service/assets/52928589/02cf5ac6-946f-460b-980b-6be163af7db7">
