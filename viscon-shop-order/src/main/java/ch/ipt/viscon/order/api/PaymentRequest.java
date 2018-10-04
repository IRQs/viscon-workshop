package ch.ipt.viscon.order.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PaymentRequest{

	public PaymentRequest(String vendor, String creditCardNumber, double amountPayable) {
		this.vendor = vendor;
		this.creditCardNumber = creditCardNumber;
		this.amountPayable = amountPayable;
	}

	@JsonProperty("vendor")
	private String vendor;

	@JsonProperty("creditCardNumber")
	private String creditCardNumber;

	@JsonProperty("amountPayable")
	private double amountPayable;

	public void setVendor(String vendor){
		this.vendor = vendor;
	}

	public String getVendor(){
		return vendor;
	}

	public void setCreditCardNumber(String creditCardNumber){
		this.creditCardNumber = creditCardNumber;
	}

	public String getCreditCardNumber(){
		return creditCardNumber;
	}

	public void setAmountPayable(double amountPayable){
		this.amountPayable = amountPayable;
	}

	public double getAmountPayable(){
		return amountPayable;
	}

	@Override
 	public String toString(){
		return
			"PaymentRequest{" +
			"vendor = '" + vendor + '\'' +
			",creditCardNumber = '" + creditCardNumber + '\'' +
			",amountPayable = '" + amountPayable + '\'' +
			"}";
		}
}
