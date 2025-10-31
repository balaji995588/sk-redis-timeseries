package com.redis_timeseries.utilityenum;

public enum ChartPeriod {

	Ticks(0), OneMinute(1), ThreeMinutes(3), FiveMinutes(5), TenMinutes(10), FifteenMinutes(15), ThirtyMinutes(30),
	SixtyMinutes(60), NinetyMinutes(90), TwoHours(120), ThreeHours(180), FourHours(240), Daily(1440), Weekly(10080),
	Monthly(43200), QUATERLY(129600), HALFYEARLY(259200), YEARLY(525600);

	private final int id;

	ChartPeriod(int id) {
		this.id = id;
	}

	public int getValue() {
		return id;
	}

	public static ChartPeriod fromInteger(int x) {
		switch (x) {
		case 0:
			return Ticks;
		case 1:
			return OneMinute;
		case 3:
			return ThreeMinutes;
		case 5:
			return FiveMinutes;
		case 10:
			return TenMinutes;
		case 15:
			return FifteenMinutes;
		case 30:
			return ThirtyMinutes;
		case 60:
			return SixtyMinutes;
		case 90:
			return NinetyMinutes;
		case 120:
			return TwoHours;
		case 180:
			return ThreeHours;
		case 240:
			return FourHours;
		case 1440:
			return Daily;
		case 10080:
			return Weekly;
		case 43200:
			return Monthly;
		case 129600:
			return QUATERLY;
		case 259200:
			return HALFYEARLY;
		case 525600:
			return YEARLY;

		}
		return null;
	}
}
