package com.project.common.util;

import com.fasterxml.uuid.Generators;

import java.util.UUID;


public class UUIDUtil {

	/**
	 * 隨機取得 32 位元的 UUID，不包含"-"
	 */
	public static String generateUuid() {
		UUID uuid = Generators.timeBasedGenerator().generate();
		return uuid.toString().replace("-", "");
	}


}
