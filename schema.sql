--
-- 資料表結構 `order`
--

CREATE TABLE `order` (
`order_id` char(50) NOT NULL,
`order_no` int NOT NULL,
`user_id` varchar(50) NOT NULL,
`concert_id` bigint NOT NULL,
`seat` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
`order_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
`time_diff_seconds` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `order`
--
ALTER TABLE `order`
    ADD PRIMARY KEY (`order_id`,`order_no`),
  ADD UNIQUE KEY `uq_concert_seat` (`concert_id`,`seat`),
  ADD KEY `seat_id` (`seat`);
COMMIT;