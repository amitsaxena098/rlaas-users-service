local key = KEYS[1]
local now = tonumber(ARGV[1])
local window = tonumber(ARGV[2]) * 1000
local limit = tonumber(ARGV[3])

redis.call("ZREMRANGEBYSCORE", key, 0, now - window)

local count = redis.call("ZCARD", key)

if count < limit then
    local member = tostring(now) .. "-" .. math.random()
     redis.call("ZADD", key, now, member)
     redis.call("PEXPIRE", key, window * 2)
    return {1, count, window/1000}
else
    redis.call("PEXPIRE", key, window * 2)
    return {0, count, window/1000}
end