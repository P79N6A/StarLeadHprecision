package com.starlead.starleadhprecision.entity.mapper;

import org.json.JSONObject;

import java.util.List;

public interface RowMapper<T> {
	public List<T> mapRow(JSONObject jsonObject) throws Exception;
}
