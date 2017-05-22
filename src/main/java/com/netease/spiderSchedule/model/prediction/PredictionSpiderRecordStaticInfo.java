package com.netease.spiderSchedule.model.prediction;

import java.util.Map;
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
	int countUp = 0;
	
	public void statistics(Entry<SpiderRecordInfo, PredictionSpiderRecordInfo> entry, long timeDelay, PredictionSpiderRateInfoService predictionSpiderRateInfoServiceImpl) {
//		if (timeDelay < 5 * 60 * 1000) {
//			count5++;
//		} else if (timeDelay >= 5 * 60 * 1000 && timeDelay < 10 * 60 * 1000) {
//			count10++;
//		} else if (timeDelay >= 10 * 60 * 1000 && timeDelay < 20 * 60 * 1000) {
//			count20++;
//		} else if (timeDelay >= 20 * 60 * 1000 && timeDelay < 30 * 60 * 1000) {
//			count30++;
//		} else if (timeDelay >= 30 * 60 * 1000 && timeDelay < 40 * 60 * 1000) {
//			count40++;
//		} else if (timeDelay >= 40 * 60 * 1000 && timeDelay < 50 * 60 * 1000) {
//			count50++;
//		} else if (timeDelay >= 50 * 60 * 1000 && timeDelay < 60 * 60 * 1000) {
//			count60++;
//		} else if (timeDelay >= 60 * 60 * 1000 && timeDelay < 70 * 60 * 1000) {
//			count70++;
//		} else if (timeDelay >= 70 * 60 * 1000 && timeDelay < 80 * 60 * 1000) {
//			count80++;
//		} else if (timeDelay >= 80 * 60 * 1000 && timeDelay < 90 * 60 * 1000) {
//			count90++;
//		} else if (timeDelay >= 90 * 60 * 1000 && timeDelay < 100 * 60 * 1000) {
//			count100++;
//		} else if (timeDelay >= 100 * 60 * 1000 && timeDelay < 110 * 60 * 1000) {
//			count110++;
//		} else if (timeDelay >= 110 * 60 * 1000 && timeDelay < 120 * 60 * 1000) {
//			count120++;
//		} else if (timeDelay >= 120 * 60 * 1000 && timeDelay < 130 * 60 * 1000) {
//			count130++;
//		} else if (timeDelay >= 130 * 60 * 1000 && timeDelay < 140 * 60 * 1000) {
//			count140++;
//		} else if (timeDelay >= 140 * 60 * 1000 && timeDelay < 150 * 60 * 1000) {
//			count150++;
//		} else if (timeDelay >= 150 * 60 * 1000 && timeDelay < 160 * 60 * 1000) {
//			count160++;
//		} else if (timeDelay >= 160 * 60 * 1000 && timeDelay < 170 * 60 * 1000) {
//			count170++;
//		} else if (timeDelay >= 170 * 60 * 1000 && timeDelay < 180 * 60 * 1000) {
//			count180++;
//		} else {
//			countUp++;
//		}
		
		
		
		
		if(timeDelay > 180 * 60 * 1000){
			countUp++;
		}else{
			count180++;
			if(timeDelay < 170 * 60 * 1000){
				count170++;
				if(timeDelay < 160 *60 * 1000) {
					count160++;
					if(timeDelay < 150 *60 * 1000) {
						count150++;
						if(timeDelay < 140 *60 * 1000) {
							count140++;
							if(timeDelay < 130 *60 * 1000) {
								count130++;
								if(timeDelay < 120 *60 * 1000) {
									count120++;
									if(timeDelay < 110 *60 * 1000) {
										count110++;
										if(timeDelay < 100 *60 * 1000) {
											count100++;
											if(timeDelay < 90 *60 * 1000) {
												count90++;
												if(timeDelay < 80 *60 * 1000) {
													count80++;
													if(timeDelay < 70 *60 * 1000) {
														count70++;
														if(timeDelay < 60 *60 * 1000) {
															count60++;
															if(timeDelay < 50 *60 * 1000) {
																count50++;
																if(timeDelay < 40 *60 * 1000) {
																	count40++;
																	if(timeDelay < 30 *60 * 1000) {
																		count30++;
																		if(timeDelay < 20 *60 * 1000) {
																			count20++;
																			if(timeDelay < 10 *60 * 1000) {
																				count10++;
																				if(timeDelay < 5 *60 * 1000) {
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
	
	
	public void statistics(long timeDelay, SpiderRecordInfo v ) {
		
		if(timeDelay > 180 * 60 * 1000){
			countUp++;
		}else{
			count180++;
			if(timeDelay < 170 * 60 * 1000){
				count170++;
				if(timeDelay < 160 *60 * 1000) {
					count160++;
					if(timeDelay < 150 *60 * 1000) {
						count150++;
						if(timeDelay < 140 *60 * 1000) {
							count140++;
							if(timeDelay < 130 *60 * 1000) {
								count130++;
								if(timeDelay < 120 *60 * 1000) {
									count120++;
									if(timeDelay < 110 *60 * 1000) {
										count110++;
										if(timeDelay < 100 *60 * 1000) {
											count100++;
											if(timeDelay < 90 *60 * 1000) {
												count90++;
												if(timeDelay < 80 *60 * 1000) {
													count80++;
													if(timeDelay < 70 *60 * 1000) {
														count70++;
														if(timeDelay < 60 *60 * 1000) {
															count60++;
															if(timeDelay < 50 *60 * 1000) {
																count50++;
																if(timeDelay < 40 *60 * 1000) {
																	count40++;
																	if(timeDelay < 30 *60 * 1000) {
																		count30++;
																		if(timeDelay < 20 *60 * 1000) {
																			count20++;
																			if(timeDelay < 10 *60 * 1000) {
																				count10++;
																				if(timeDelay < 5 *60 * 1000) {
																					count5++;
																				}
																			}
																		}
																	}else{
																		System.out.println(v);
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


	@Override
	public String toString() {
		int countAll = count180+countUp;
		System.out.println("countAll:" + countAll);
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 5 minute is " + (count5 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 10 minute is " + (count10 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 20 minute is " + (count20 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 30 minute is " + (count30 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 40 minute is " + (count40 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 50 minute is " + (count50 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 60 minute is " + (count60 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 70 minute is " + (count70 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 80 minute is " + (count80 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 90 minute is " + (count90 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 100 minute is " + (count100 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 110 minute is " + (count110 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 120 minute is " + (count120 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 130 minute is " + (count130* 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 140 minute is " + (count140 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 150 minute is " + (count150 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 160 minute is " + (count160 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 170 minute is " + (count170 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of low 180 minute is " + (count180 * 100d /countAll) + "%");
		System.out.println("PredictionSpiderRecordStaticInfo the rate of up 180 minute is " + (countUp * 100d /countAll) + "%");
		return "PredictionSpiderRecordStaticInfo [count5=" + count5 + ", count10=" + count10 + ", count20=" + count20
				+ ", count30=" + count30 + ", count40=" + count40 + ", count50=" + count50 + ", count60=" + count60
				+ ", count70=" + count70 + ", count80=" + count80 + ", count90=" + count90 + ", count100=" + count100
				+ ", count110=" + count110 + ", count120=" + count120 + ", count130=" + count130 + ", count140="
				+ count140 + ", count150=" + count150 + ", count160=" + count160 + ", count170=" + count170
				+ ", count180=" + count180 + ", countUp=" + countUp + "]";
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


	public int getCountUp() {
		return countUp;
	}


	public void setCountUp(int countUp) {
		this.countUp = countUp;
	}
	
	


}
