package issuer.rest;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class CompletePaymentDTO {

	private int acquirerOrderId; 
	private Date acquirerTimestamp; 
	private String pan; 
	private String securityCode; 
	private String cardHolderName; 
	private Date expiration; //samo mesec i godinu izvlaci
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal amount; 
	public CompletePaymentDTO() {
		
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public int getAcquirerOrderId() {
		return acquirerOrderId;
	}

	public void setAcquirerOrderId(int acquirerOrderId) {
		this.acquirerOrderId = acquirerOrderId;
	}

	public Date getAcquirerTimestamp() {
		return acquirerTimestamp;
	}

	public void setAcquirerTimestamp(Date acquirerTimestamp) {
		this.acquirerTimestamp = acquirerTimestamp;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}	
}
