package org.pgcl.dto;

import org.pgcl.enums.MeterMeasurementType;
import org.pgcl.enums.ReadingPurpose;

import com.google.gson.Gson;

public class MeterReadingDTO {

	private String reading_id;
	private String customer_id;
	private String customer_name;
	private String address;
	private String meter_id;
	private String meter_sl_no;
	private ReadingPurpose reading_purpose;
	private String reading_purpose_str;
	private String reading_purpose_name;
	private int billing_month;
	private int billing_year;
	private float prev_reading;
	private String prev_reading_date;
	private float curr_reading;
	private String curr_reading_date;
	private float hhv_nhv;
	private MeterMeasurementType measurement_type;
	private String measurement_type_str;
	private String measurement_type_name;
	private int tariff_id;
	private int latest_tariff_id;
	private float rate;
	private float latest_rate;
	private float difference;
	private float min_load;
	private float max_load;
	private float percent;
	private float pMin_load; //Proportional Min Load
	private float pMax_load; //Proportional Max Load
	private double actual_consumption;
	private double total_consumption;
	private float meter_rent;
	private float pressure;
	private double pressure_factor;
	private float temperature;
	private double temperature_factor;
	private float pressure_latest;
	private double pressure_factor_latest;
	private float temperature_latest;
	private double temperature_factor_latest; //latest pre/temp is used when pressure is change middle of the moth to get the latest one pressure
	private String remarks;
	private String insert_date;
	private String insert_by;
	private String month_year;
	private String bill_id;
	private String month;
	private String year;
	private String prevReadingDate;
	private String currentReadinDate;
	private int total_reading_count;
	private int current_reading_index;
	private int burnerQty;
	private String previous_reading_date;
	private String current_reading_date;
	
	private CustomerDTO customer;
	
	
	
	
	public String getPrevious_reading_date() {
		return previous_reading_date;
	}
	public void setPrevious_reading_date(String previous_reading_date) {
		this.previous_reading_date = previous_reading_date;
	}
	public String getCurrent_reading_date() {
		return current_reading_date;
	}
	public void setCurrent_reading_date(String current_reading_date) {
		this.current_reading_date = current_reading_date;
	}
	public int getBurnerQty() {
		return burnerQty;
	}
	public void setBurnerQty(int burnerQty) {
		this.burnerQty = burnerQty;
	}
	public int getLatest_tariff_id() {
		return latest_tariff_id;
	}
	public void setLatest_tariff_id(int latest_tariff_id) {
		this.latest_tariff_id = latest_tariff_id;
	}
	public float getLatest_rate() {
		return latest_rate;
	}
	public void setLatest_rate(float latest_rate) {
		this.latest_rate = latest_rate;
	}
	public float getPressure_latest() {
		return pressure_latest;
	}
	public void setPressure_latest(float pressure_latest) {
		this.pressure_latest = pressure_latest;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPrevReadingDate() {
		return prevReadingDate;
	}
	public void setPrevReadingDate(String prevReadingDate) {
		this.prevReadingDate = prevReadingDate;
	}
	public String getCurrentReadinDate() {
		return currentReadinDate;
	}
	public void setCurrentReadinDate(String currentReadinDate) {
		this.currentReadinDate = currentReadinDate;
	}
	public double getPressure_factor_latest() {
		return pressure_factor_latest;
	}
	public void setPressure_factor_latest(double pressure_factor_latest) {
		this.pressure_factor_latest = pressure_factor_latest;
	}
	public float getTemperature_latest() {
		return temperature_latest;
	}
	public void setTemperature_latest(float temperature_latest) {
		this.temperature_latest = temperature_latest;
	}
	public double getTemperature_factor_latest() {
		return temperature_factor_latest;
	}
	public void setTemperature_factor_latest(double temperature_factor_latest) {
		this.temperature_factor_latest = temperature_factor_latest;
	}
	public String getReading_id() {
		return reading_id;
	}
	public void setReading_id(String readingId) {
		reading_id = readingId;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customerId) {
		customer_id = customerId;
	}	
	public String getMeter_id() {
		return meter_id;
	}
	public void setMeter_id(String meterId) {
		meter_id = meterId;
	}
	public String getMeter_sl_no() {
		return meter_sl_no;
	}
	public void setMeter_sl_no(String meterSlNo) {
		meter_sl_no = meterSlNo;
	}
	public ReadingPurpose getReading_purpose() {
		return reading_purpose;
	}
	public void setReading_purpose(ReadingPurpose readingPurpose) {
		reading_purpose = readingPurpose;
	}
	public int getBilling_month() {
		return billing_month;
	}
	public void setBilling_month(int billingMonth) {
		billing_month = billingMonth;
	}
	public int getBilling_year() {
		return billing_year;
	}
	public void setBilling_year(int billingYear) {
		billing_year = billingYear;
	}
	public float getPrev_reading() {
		return prev_reading;
	}
	public void setPrev_reading(float prevReading) {
		prev_reading = prevReading;
	}
	public String getPrev_reading_date() {
		return prev_reading_date;
	}
	public void setPrev_reading_date(String prevReadingDate) {
		prev_reading_date = prevReadingDate;
	}
	public float getCurr_reading() {
		return curr_reading;
	}
	public void setCurr_reading(float currReading) {
		curr_reading = currReading;
	}
	public String getCurr_reading_date() {
		return curr_reading_date;
	}
	public void setCurr_reading_date(String currReadingDate) {
		curr_reading_date = currReadingDate;
	}
	public float getHhv_nhv() {
		return hhv_nhv;
	}
	public void setHhv_nhv(float hhvNhv) {
		hhv_nhv = hhvNhv;
	}		
	public MeterMeasurementType getMeasurement_type() {
		return measurement_type;
	}
	public void setMeasurement_type(MeterMeasurementType measurementType) {
		measurement_type = measurementType;
	}
	public String getMeasurement_type_str() {
		return measurement_type_str;
	}
	public void setMeasurement_type_str(String measurementTypeStr) {
		measurement_type_str = measurementTypeStr;
	}
	public String getMeasurement_type_name() {
		return measurement_type_name;
	}
	public void setMeasurement_type_name(String measurementTypeName) {
		measurement_type_name = measurementTypeName;
	}
	public int getTariff_id() {
		return tariff_id;
	}
	public void setTariff_id(int tariffId) {
		tariff_id = tariffId;
	}
	public float getRate() {
		return rate;
	}
	public void setRate(float rate) {
		this.rate = rate;
	}
	public float getDifference() {
		return difference;
	}
	public void setDifference(float difference) {
		this.difference = difference;
	}
	public float getMin_load() {
		return min_load;
	}
	public void setMin_load(float minLoad) {
		min_load = minLoad;
	}
	public float getMax_load() {
		return max_load;
	}
	public void setMax_load(float maxLoad) {
		max_load = maxLoad;
	}
	public float getMeter_rent() {
		return meter_rent;
	}
	public void setMeter_rent(float meterRent) {
		meter_rent = meterRent;
	}
	public float getPressure() {
		return pressure;
	}
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public double getPressure_factor() {
		return pressure_factor;
	}
	public void setPressure_factor(double pressure_factor) {
		this.pressure_factor = pressure_factor;
	}
	public double getTemperature_factor() {
		return temperature_factor;
	}
	public void setTemperature_factor(double temperature_factor) {
		this.temperature_factor = temperature_factor;
	}
	public String getReading_purpose_str() {
		return reading_purpose_str;
	}
	public void setReading_purpose_str(String readingPurposeStr) {
		reading_purpose_str = readingPurposeStr;
	}
	public String getReading_purpose_name() {
		return reading_purpose_name;
	}
	public void setReading_purpose_name(String readingPurposeName) {
		reading_purpose_name = readingPurposeName;
	}	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	
	public String getInsert_date() {
		return insert_date;
	}
	public void setInsert_date(String insertDate) {
		insert_date = insertDate;
	}
	public String getInsert_by() {
		return insert_by;
	}
	public void setInsert_by(String insertBy) {
		insert_by = insertBy;
	}
	public String getMonth_year() {
		return month_year;
	}
	public void setMonth_year(String monthYear) {
		month_year = monthYear;
	}
	
	public String getBill_id() {
		return bill_id;
	}
	public void setBill_id(String billId) {
		bill_id = billId;
	}
	
	public int getTotal_reading_count() {
		return total_reading_count;
	}
	public void setTotal_reading_count(int totalReadingCount) {
		total_reading_count = totalReadingCount;
	}
	public int getCurrent_reading_index() {
		return current_reading_index;
	}
	public void setCurrent_reading_index(int currentReadingIndex) {
		current_reading_index = currentReadingIndex;
	}
	
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customerName) {
		customer_name = customerName;
	}
	
	public float getpMin_load() {
		return pMin_load;
	}
	public void setpMin_load(float pMinLoad) {
		pMin_load = pMinLoad;
	}
	public float getpMax_load() {
		return pMax_load;
	}
	public void setpMax_load(float pMaxLoad) {
		pMax_load = pMaxLoad;
	}
	
	public double getActual_consumption() {
		return actual_consumption;
	}
	public double getTotal_consumption() {
		return total_consumption;
	}
	public void setActual_consumption(double actual_consumption) {
		this.actual_consumption = actual_consumption;
	}
	public void setTotal_consumption(double total_consumption) {
		this.total_consumption = total_consumption;
	}
	public float getPercent() {
		return percent;
	}
	public void setPercent(float percent) {
		this.percent = percent;
	}	
	public String toString() {         
        Gson gson = new Gson();
		return gson.toJson(this);
    }
}
