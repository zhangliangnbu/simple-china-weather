package com.tech.heathcilff.simplechinaweather.entity;

/**
 *
 * Created by zhangliang on 11/04/2017.
 */

public class BaiduCityInfo {
	public Result result;
	public String status;

	public class Result {
		public PoiRegions[] poiRegions;
		public AddressComponent addressComponent;
		public Location location;
		public String cityCode;
		public String formatted_address;
		public String[] pois;
		public String sematic_description;
		public String business;
	}

	public class PoiRegions {
		public String tag;
		public String name;
		public String direction_desc;
	}

	public class AddressComponent {
		public String distance;
		public String direction;
		public String street;
		public String province;
		public String adcode;
		public String street_number;
		public String district;
		public String country_code;
		public String city;
		public String country;
	}

	public class Location {
		public String lng;
		public String lat;
	}

	public String getCity() {
		if(result == null || result.addressComponent == null) {
			return "";
		}
		return result.addressComponent.city;
	}
}
