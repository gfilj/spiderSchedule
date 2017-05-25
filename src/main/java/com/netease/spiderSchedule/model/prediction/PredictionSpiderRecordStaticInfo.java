package com.netease.spiderSchedule.model.prediction;

import java.util.Map.Entry;

import com.netease.spiderSchedule.model.SpiderRecordInfo;
import com.netease.spiderSchedule.service.spiderRateInfo.PredictionSpiderRateInfoService;

public class PredictionSpiderRecordStaticInfo {

	int count5 = 0;
	int count10 = 0;
	int count20 = 0;
	int count30 = 0;
	int count40 = 0;
	int count50 = 0;
	int count60 = 0;
	int count70 = 0;
	int count80 = 0;
	int count90 = 0;
	int count100 = 0;
	int count110 = 0;
	int count120 = 0;
	int count130 = 0;
	int count140 = 0;
	int count150 = 0;
	int count160 = 0;
	int count170 = 0;
	int count180 = 0;
	int count190 = 0;
	int count200 = 0;
	int count210 = 0;
	int count220 = 0;
	int count230 = 0;
	int count240 = 0;
	int countAll = 0;

	double count5Percent = 0;
	double count10Percent = 0;
	double count20Percent = 0;
	double count30Percent = 0;
	double count40Percent = 0;
	double count50Percent = 0;
	double count60Percent = 0;
	double count70Percent = 0;
	double count80Percent = 0;
	double count90Percent = 0;
	double count100Percent = 0;
	double count110Percent = 0;
	double count120Percent = 0;
	double count130Percent = 0;
	double count140Percent = 0;
	double count150Percent = 0;
	double count160Percent = 0;
	double count170Percent = 0;
	double count180Percent = 0;
	double count190Percent = 0;
	double count200Percent = 0;
	double count210Percent = 0;
	double count220Percent = 0;
	double count230Percent = 0;
	double count240Percent = 0;
	double countAllPercent = 0;

	public void statistics(Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry, long timeDelay,
			PredictionSpiderRateInfoService predictionSpiderRateInfoServiceImpl) {
		// if (timeDelay < 5 * 60 * 1000) {
		// count5++;
		// } else if (timeDelay >= 5 * 60 * 1000 && timeDelay < 10 * 60 * 1000)
		// {
		// count10++;
		// } else if (timeDelay >= 10 * 60 * 1000 && timeDelay < 20 * 60 * 1000)
		// {
		// count20++;
		// } else if (timeDelay >= 20 * 60 * 1000 && timeDelay < 30 * 60 * 1000)
		// {
		// count30++;
		// } else if (timeDelay >= 30 * 60 * 1000 && timeDelay < 40 * 60 * 1000)
		// {
		// count40++;
		// } else if (timeDelay >= 40 * 60 * 1000 && timeDelay < 50 * 60 * 1000)
		// {
		// count50++;
		// } else if (timeDelay >= 50 * 60 * 1000 && timeDelay < 60 * 60 * 1000)
		// {
		// count60++;
		// } else if (timeDelay >= 60 * 60 * 1000 && timeDelay < 70 * 60 * 1000)
		// {
		// count70++;
		// } else if (timeDelay >= 70 * 60 * 1000 && timeDelay < 80 * 60 * 1000)
		// {
		// count80++;
		// } else if (timeDelay >= 80 * 60 * 1000 && timeDelay < 90 * 60 * 1000)
		// {
		// count90++;
		// } else if (timeDelay >= 90 * 60 * 1000 && timeDelay < 100 * 60 *
		// 1000) {
		// count100++;
		// } else if (timeDelay >= 100 * 60 * 1000 && timeDelay < 110 * 60 *
		// 1000) {
		// count110++;
		// } else if (timeDelay >= 110 * 60 * 1000 && timeDelay < 120 * 60 *
		// 1000) {
		// count120++;
		// } else if (timeDelay >= 120 * 60 * 1000 && timeDelay < 130 * 60 *
		// 1000) {
		// count130++;
		// } else if (timeDelay >= 130 * 60 * 1000 && timeDelay < 140 * 60 *
		// 1000) {
		// count140++;
		// } else if (timeDelay >= 140 * 60 * 1000 && timeDelay < 150 * 60 *
		// 1000) {
		// count150++;
		// } else if (timeDelay >= 150 * 60 * 1000 && timeDelay < 160 * 60 *
		// 1000) {
		// count160++;
		// } else if (timeDelay >= 160 * 60 * 1000 && timeDelay < 170 * 60 *
		// 1000) {
		// count170++;
		// } else if (timeDelay >= 170 * 60 * 1000 && timeDelay < 180 * 60 *
		// 1000) {
		// count180++;
		// } else {
		// countUp++;
		// }

		countAll++;
		if (timeDelay <= 240 * 60 * 1000) {
			count240++;
			if (timeDelay <= 230 * 60 * 1000) {
				count230++;
				if (timeDelay <= 220 * 60 * 1000) {
					count220++;
					if (timeDelay <= 210 * 60 * 1000) {
						count210++;
						if (timeDelay <= 200 * 60 * 1000) {
							count200++;
							if (timeDelay <= 190 * 60 * 1000) {
								count190++;
								if (timeDelay <= 180 * 60 * 1000) {
									count180++;
									if (timeDelay <= 170 * 60 * 1000) {
										count170++;
										if (timeDelay <= 160 * 60 * 1000) {
											count160++;
											if (timeDelay <= 150 * 60 * 1000) {
												count150++;
												if (timeDelay <= 140 * 60 * 1000) {
													count140++;
													if (timeDelay <= 130 * 60 * 1000) {
														count130++;
														if (timeDelay <= 120 * 60 * 1000) {
															count120++;
															if (timeDelay <= 110 * 60 * 1000) {
																count110++;
																if (timeDelay <= 100 * 60 * 1000) {
																	count100++;
																	if (timeDelay <= 90 * 60 * 1000) {
																		count90++;
																		if (timeDelay <= 80 * 60 * 1000) {
																			count80++;
																			if (timeDelay <= 70 * 60 * 1000) {
																				count70++;
																				if (timeDelay <= 60 * 60 * 1000) {
																					count60++;
																					if (timeDelay <= 50 * 60 * 1000) {
																						count50++;
																						if (timeDelay <= 40 * 60
																								* 1000) {
																							count40++;
																							if (timeDelay <= 30 * 60
																									* 1000) {
																								count30++;
																								if (timeDelay <= 20 * 60
																										* 1000) {
																									count20++;
																									if (timeDelay <= 10
																											* 60
																											* 1000) {
																										count10++;
																										if (timeDelay <= 5
																												* 60
																												* 1000) {
																											count5++;
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		generatePercent();
	}

	public void statistics(long timeDelay, SpiderRecordInfo v) {

		countAll++;
		if (timeDelay <= 240 * 60 * 1000) {
			count240++;
			if (timeDelay <= 230 * 60 * 1000) {
				count230++;
				if (timeDelay <= 220 * 60 * 1000) {
					count220++;
					if (timeDelay <= 210 * 60 * 1000) {
						count210++;
						if (timeDelay <= 200 * 60 * 1000) {
							count200++;
							if (timeDelay <= 190 * 60 * 1000) {
								count190++;
								if (timeDelay <= 180 * 60 * 1000) {
									count180++;
									if (timeDelay <= 170 * 60 * 1000) {
										count170++;
										if (timeDelay <= 160 * 60 * 1000) {
											count160++;
											if (timeDelay <= 150 * 60 * 1000) {
												count150++;
												if (timeDelay <= 140 * 60 * 1000) {
													count140++;
													if (timeDelay <= 130 * 60 * 1000) {
														count130++;
														if (timeDelay <= 120 * 60 * 1000) {
															count120++;
															if (timeDelay <= 110 * 60 * 1000) {
																count110++;
																if (timeDelay <= 100 * 60 * 1000) {
																	count100++;
																	if (timeDelay <= 90 * 60 * 1000) {
																		count90++;
																		if (timeDelay <= 80 * 60 * 1000) {
																			count80++;
																			if (timeDelay <= 70 * 60 * 1000) {
																				count70++;
																				if (timeDelay <= 60 * 60 * 1000) {
																					count60++;
																					if (timeDelay <= 50 * 60 * 1000) {
																						count50++;
																						if (timeDelay <= 40 * 60
																								* 1000) {
																							count40++;
																							if (timeDelay <= 30 * 60
																									* 1000) {
																								count30++;
																								if (timeDelay <= 20 * 60
																										* 1000) {
																									count20++;
																									if (timeDelay <= 10
																											* 60
																											* 1000) {
																										count10++;
																										if (timeDelay <= 5
																												* 60
																												* 1000) {
																											count5++;
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		generatePercent();
	}

	public void generatePercent() {
		count5Percent = count5 * 100d / countAll;
		count10Percent = count10 * 100d / countAll;
		count20Percent = count20 * 100d / countAll;
		count30Percent = count30 * 100d / countAll;
		count40Percent = count40 * 100d / countAll;
		count50Percent = count50 * 100d / countAll;
		count60Percent = count60 * 100d / countAll;
		count70Percent = count70 * 100d / countAll;
		count80Percent = count80 * 100d / countAll;
		count90Percent = count90 * 100d / countAll;
		count100Percent = count100 * 100d / countAll;
		count110Percent = count110 * 100d / countAll;
		count120Percent = count120 * 100d / countAll;
		count130Percent = count130 * 100d / countAll;
		count140Percent = count140 * 100d / countAll;
		count150Percent = count150 * 100d / countAll;
		count160Percent = count160 * 100d / countAll;
		count170Percent = count170 * 100d / countAll;
		count180Percent = count180 * 100d / countAll;
		count190Percent = count190 * 100d / countAll;
		count200Percent = count200 * 100d / countAll;
		count210Percent = count210 * 100d / countAll;
		count220Percent = count220 * 100d / countAll;
		count230Percent = count230 * 100d / countAll;
		count240Percent = count240 * 100d / countAll;
		countAllPercent = countAll * 100d / countAll;
	}

	@Override
	public String toString() {
		System.out.println("countAll:" + countAll);
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 5 minute is " + count5Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 10 minute is " + count10Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 20 minute is " + count20Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 30 minute is " + count30Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 40 minute is " + count40Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 50 minute is " + count50Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 60 minute is " + count60Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 70 minute is " + count70Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 80 minute is " + count80Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 90 minute is " + count90Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 100 minute is " + count100Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 110 minute is " + count110Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 120 minute is " + count120Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 130 minute is " + count130Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 140 minute is " + count140Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 150 minute is " + count150Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 160 minute is " + count160Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 170 minute is " + count170Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 180 minute is " + count180Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 190 minute is " + count190Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 200 minute is " + count200Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 210 minute is " + count210Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 220 minute is " + count220Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 230 minute is " + count230Percent + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 240 minute is " + count240Percent + "%");
		
		return "";
	}

	public int getCount5() {
		return count5;
	}

	public void setCount5(int count5) {
		this.count5 = count5;
	}

	public int getCount10() {
		return count10;
	}

	public void setCount10(int count10) {
		this.count10 = count10;
	}

	public int getCount20() {
		return count20;
	}

	public void setCount20(int count20) {
		this.count20 = count20;
	}

	public int getCount30() {
		return count30;
	}

	public void setCount30(int count30) {
		this.count30 = count30;
	}

	public int getCount40() {
		return count40;
	}

	public void setCount40(int count40) {
		this.count40 = count40;
	}

	public int getCount50() {
		return count50;
	}

	public void setCount50(int count50) {
		this.count50 = count50;
	}

	public int getCount60() {
		return count60;
	}

	public void setCount60(int count60) {
		this.count60 = count60;
	}

	public int getCount70() {
		return count70;
	}

	public void setCount70(int count70) {
		this.count70 = count70;
	}

	public int getCount80() {
		return count80;
	}

	public void setCount80(int count80) {
		this.count80 = count80;
	}

	public int getCount90() {
		return count90;
	}

	public void setCount90(int count90) {
		this.count90 = count90;
	}

	public int getCount100() {
		return count100;
	}

	public void setCount100(int count100) {
		this.count100 = count100;
	}

	public int getCount110() {
		return count110;
	}

	public void setCount110(int count110) {
		this.count110 = count110;
	}

	public int getCount120() {
		return count120;
	}

	public void setCount120(int count120) {
		this.count120 = count120;
	}

	public int getCount130() {
		return count130;
	}

	public void setCount130(int count130) {
		this.count130 = count130;
	}

	public int getCount140() {
		return count140;
	}

	public void setCount140(int count140) {
		this.count140 = count140;
	}

	public int getCount150() {
		return count150;
	}

	public void setCount150(int count150) {
		this.count150 = count150;
	}

	public int getCount160() {
		return count160;
	}

	public void setCount160(int count160) {
		this.count160 = count160;
	}

	public int getCount170() {
		return count170;
	}

	public void setCount170(int count170) {
		this.count170 = count170;
	}

	public int getCount180() {
		return count180;
	}

	public void setCount180(int count180) {
		this.count180 = count180;
	}


	public int getCountAll() {
		return countAll;
	}

	public void setCountAll(int countAll) {
		this.countAll = countAll;
	}

	public double getCount5Percent() {
		return count5Percent;
	}

	public void setCount5Percent(double count5Percent) {
		this.count5Percent = count5Percent;
	}

	public double getCount10Percent() {
		return count10Percent;
	}

	public void setCount10Percent(double count10Percent) {
		this.count10Percent = count10Percent;
	}

	public double getCount20Percent() {
		return count20Percent;
	}

	public void setCount20Percent(double count20Percent) {
		this.count20Percent = count20Percent;
	}

	public double getCount30Percent() {
		return count30Percent;
	}

	public void setCount30Percent(double count30Percent) {
		this.count30Percent = count30Percent;
	}

	public double getCount40Percent() {
		return count40Percent;
	}

	public void setCount40Percent(double count40Percent) {
		this.count40Percent = count40Percent;
	}

	public double getCount50Percent() {
		return count50Percent;
	}

	public void setCount50Percent(double count50Percent) {
		this.count50Percent = count50Percent;
	}

	public double getCount60Percent() {
		return count60Percent;
	}

	public void setCount60Percent(double count60Percent) {
		this.count60Percent = count60Percent;
	}

	public double getCount70Percent() {
		return count70Percent;
	}

	public void setCount70Percent(double count70Percent) {
		this.count70Percent = count70Percent;
	}

	public double getCount80Percent() {
		return count80Percent;
	}

	public void setCount80Percent(double count80Percent) {
		this.count80Percent = count80Percent;
	}

	public double getCount90Percent() {
		return count90Percent;
	}

	public void setCount90Percent(double count90Percent) {
		this.count90Percent = count90Percent;
	}

	public double getCount100Percent() {
		return count100Percent;
	}

	public void setCount100Percent(double count100Percent) {
		this.count100Percent = count100Percent;
	}

	public double getCount110Percent() {
		return count110Percent;
	}

	public void setCount110Percent(double count110Percent) {
		this.count110Percent = count110Percent;
	}

	public double getCount120Percent() {
		return count120Percent;
	}

	public void setCount120Percent(double count120Percent) {
		this.count120Percent = count120Percent;
	}

	public double getCount130Percent() {
		return count130Percent;
	}

	public void setCount130Percent(double count130Percent) {
		this.count130Percent = count130Percent;
	}

	public double getCount140Percent() {
		return count140Percent;
	}

	public void setCount140Percent(double count140Percent) {
		this.count140Percent = count140Percent;
	}

	public double getCount150Percent() {
		return count150Percent;
	}

	public void setCount150Percent(double count150Percent) {
		this.count150Percent = count150Percent;
	}

	public double getCount160Percent() {
		return count160Percent;
	}

	public void setCount160Percent(double count160Percent) {
		this.count160Percent = count160Percent;
	}

	public double getCount170Percent() {
		return count170Percent;
	}

	public void setCount170Percent(double count170Percent) {
		this.count170Percent = count170Percent;
	}

	public double getCount180Percent() {
		return count180Percent;
	}

	public void setCount180Percent(double count180Percent) {
		this.count180Percent = count180Percent;
	}

	public int getCount190() {
		return count190;
	}

	public void setCount190(int count190) {
		this.count190 = count190;
	}

	public int getCount200() {
		return count200;
	}

	public void setCount200(int count200) {
		this.count200 = count200;
	}

	public int getCount210() {
		return count210;
	}

	public void setCount210(int count210) {
		this.count210 = count210;
	}

	public int getCount220() {
		return count220;
	}

	public void setCount220(int count220) {
		this.count220 = count220;
	}

	public int getCount230() {
		return count230;
	}

	public void setCount230(int count230) {
		this.count230 = count230;
	}

	public int getCount240() {
		return count240;
	}

	public void setCount240(int count240) {
		this.count240 = count240;
	}

	public double getCount190Percent() {
		return count190Percent;
	}

	public void setCount190Percent(double count190Percent) {
		this.count190Percent = count190Percent;
	}

	public double getCount200Percent() {
		return count200Percent;
	}

	public void setCount200Percent(double count200Percent) {
		this.count200Percent = count200Percent;
	}

	public double getCount210Percent() {
		return count210Percent;
	}

	public void setCount210Percent(double count210Percent) {
		this.count210Percent = count210Percent;
	}

	public double getCount220Percent() {
		return count220Percent;
	}

	public void setCount220Percent(double count220Percent) {
		this.count220Percent = count220Percent;
	}

	public double getCount230Percent() {
		return count230Percent;
	}

	public void setCount230Percent(double count230Percent) {
		this.count230Percent = count230Percent;
	}

	public double getCount240Percent() {
		return count240Percent;
	}

	public void setCount240Percent(double count240Percent) {
		this.count240Percent = count240Percent;
	}

	public double getCountAllPercent() {
		return countAllPercent;
	}

	public void setCountAllPercent(double countAllPercent) {
		this.countAllPercent = countAllPercent;
	}
	
}
