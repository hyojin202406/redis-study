# Redis Study
## 개요

---

RedisTemplate을 사용하여 Redis의 Sorted Set을 활용합니다. <br/>
Sorted Set은 각 항목에 score를 부여해 자동으로 정렬되므로, 대기열에서 사용자 우선순위를 관리하기에 유용합니다.
 <br/> <br/>

## Redis CLI 접속

---

- Redis CLI 접속: `docker exec -it redis-container redis-cli`, `redis-cli`
- Sorted Set의 값 조회: `ZRANGE userPriorityQueue 0 -1 WITHSCORES`
- 특정 범위의 사용자 확인: `ZRANGE userPriorityQueue 0 4 WITHSCORES`
- 리스트에서 특정 요소 삭제 : `LREM activeUserQueue 0 "user1"`
 <br/> <br/>

## 대기열 서비스 구현

---
- [REST_API](docs/REST_API.md)
- [Sequence_Diagram](docs/Sequence_Diagram.md)
### 시나리오
- 사용자가 접속할 때마다 해당 사용자를 저장합니다
- 스케줄러를 통해 10명씩 활성 유저 대기열에 넣습니다 
- 각 사용자에 대해 10분의 TTL(생존 시간)을 설정합니다

### Redis 명령어로 상태 확인
- 대기열의 상태 확인: `ZRANGE userPriorityQueue 0 -1 WITHSCORES`
- 활성 사용자 대기열 확인: `LRANGE activeUserQueue 0 -1`