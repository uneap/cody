## redis

FROM redis:7.2.5-alpine3.19
EXPOSE 6379
CMD ["redis-server"]