-- KEYS[1] 是 seatAreaKey，表示座位區域的 Redis 鍵，例如 "concert:1-A-{k2}"
-- ARGV[1] 是要彈出的座位數量

-- 從 KEYS 和 ARGV 中獲取參數
local seatAreaKey = KEYS[1]           -- 座位區域的 Redis 鍵
local quantity = tonumber(ARGV[1])    -- 要彈出的座位數量，轉換為數字類型

-- 初始化一個空表，用於存儲彈出的座位
local seats = {}

-- 循環彈出指定數量的座位
for i = 1, quantity do
    -- 從左側彈出一個座位
    local seat = redis.call('LPOP', seatAreaKey)

    -- 如果成功彈出座位，將其加入 seats 表中
    if seat then
        table.insert(seats, seat)
    else
        -- 如果無法彈出更多座位（例如列表已空），終止循環
        break
    end
end

-- 檢查彈出的座位數量是否滿足需求
if #seats < quantity then
    -- 座位不足，將已彈出的座位放回 Redis 列表
    for _, seat in ipairs(seats) do
        redis.call('RPUSH', seatAreaKey, seat)
    end
    -- 返回一個空表，表示操作失敗
    return {}
else
    -- 座位充足，返回彈出的座位列表
    return seats
end
