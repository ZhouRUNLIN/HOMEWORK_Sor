
local username = KEYS[1]
local timeWindow = tonumber(ARGV[1])


local accessKey = "short-link:user-flow-risk-control:" .. username


local currentAccessCount = redis.call("INCR", accessKey)


if currentAccessCount == 1 then
    redis.call("EXPIRE", accessKey, timeWindow)
end


return currentAccessCount