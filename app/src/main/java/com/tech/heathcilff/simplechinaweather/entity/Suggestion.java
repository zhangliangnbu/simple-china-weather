package com.tech.heathcilff.simplechinaweather.entity;

/**
 * Created by zhangliang on 2017/2/28.
 */

public class Suggestion {
	public SingleSuggestion air;
	public SingleSuggestion comf;
	public SingleSuggestion cw;
	public SingleSuggestion drsg;
	public SingleSuggestion flu;
	public SingleSuggestion sport;
	public SingleSuggestion trav;
	public SingleSuggestion uv;

	public class SingleSuggestion {
		public String brf;
		public String txt;
	}
}
