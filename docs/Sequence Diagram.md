``` mermaid
sequenceDiagram
    participant User as 사용자
    participant Service as PriorityQueueService
    participant Redis as Redis

    
    User->>Service: 대기열 추가(Sorted set)
    Service->>Redis: ZADD userPriorityQueue(userId, priorityScore)
    Redis-->>Service: 응답
    Service-->>User: 응답
    
    rect rgb(200, 200, 255)
        Note left of Redis: 활성 유저 대기열 추가 스케줄러
        Scheduler->>Redis: 대기열 큐 조회: ZRANGE userPriorityQueue(0, 2)
        Redis-->>Scheduler: 3명 사용자 리스트 반환
        Scheduler->>Redis: 활성큐 추가: RPush activeUserQueue(user)
        Scheduler->>Redis: 대기열 사용자 제거: ZREM userPriorityQueue(user)
        Redis-->>Scheduler: 응답
    end
```
