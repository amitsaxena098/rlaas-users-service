local windowSize = tonumber(ARGV[1])
local limit = tonumber(ARGV[2])
local now = redis.call('TIME')
local currentSeconds = tonumber(now[1])
local window = math.floor(currentSeconds / windowSize)
local key = KEYS[1] .. ':' .. window
local current = redis.call('INCR', key)
if current == 1 then
  redis.call('EXPIRE', key, windowSize)
end
local ttl = redis.call('TTL', key)
if current > limit then
  return {0, current, ttl}
else
  return {1, current, ttl}
end