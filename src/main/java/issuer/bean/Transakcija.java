package issuer.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;

import javax.persistence.Id;

@Entity
public class Transakcija implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    private int transactionIdAquirer; 

    @Column(nullable = false)
    private StanjeTransakcije stanje;
    
    @Column(nullable = false)
    private Date vreme; 
    
    @Column(nullable = false)
    private Date acquirerTimestamp; 

    @ManyToOne(fetch = FetchType.EAGER)
    private IssuedCard issuedCard; 
    
    @Digits(integer=10, fraction=2)
    private BigDecimal amount; 
    
    @Column(nullable = true)
    private String errorUrl;
    
    public Transakcija(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTransactionIdAquirer() {
		return transactionIdAquirer;
	}

	public void setTransactionIdAquirer(int transactionIdAquirer) {
		this.transactionIdAquirer = transactionIdAquirer;
	}

	public StanjeTransakcije getStanje() {
		return stanje;
	}

	public void setStanje(StanjeTransakcije stanje) {
		this.stanje = stanje;
	}

	public Date getVreme() {
		return vreme;
	}

	public void setVreme(Date vreme) {
		this.vreme = vreme;
	}

	public Date getAcquirerTimestamp() {
		return acquirerTimestamp;
	}

	public void setAcquirerTimestamp(Date acquirerTimestamp) {
		this.acquirerTimestamp = acquirerTimestamp;
	}

	public IssuedCard getIssuedCard() {
		return issuedCard;
	}

	public void setIssuedCard(IssuedCard issuedCard) {
		this.issuedCard = issuedCard;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getErrorUrl() {
		return errorUrl;
	}

	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}	
}
