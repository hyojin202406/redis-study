# Redis 연습
RedisTemplate을 사용하여 Redis의 Sorted Set을 활용할 수 있습니다. <br/>
Sorted Set은 각 항목에 score를 부여해 자동으로 정렬되므로, 대기열에서 사용자 우선순위를 관리하기에 유용합니다.

---
## Redis CLI 접속
- Redis CLI 접속: `docker exec -it redis-container redis-cli`, `redis-cli`
- Sorted Set의 값 조회: `ZRANGE userPriorityQueue 0 -1 WITHSCORES`
- 특정 범위의 사용자 확인: `ZRANGE userPriorityQueue 0 4 WITHSCORES`