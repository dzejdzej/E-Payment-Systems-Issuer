package issuer.rest;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import issuer.bean.CardHolder;
import issuer.bean.IssuedCard;
import issuer.repository.CardHolderRepository;
import issuer.repository.IssuedCardRepository;
import issuer.repository.TransakcijaRepository;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private IssuedCardRepository issuedCardRepository; 

	@Autowired
	private TransakcijaRepository transakcijaRepository; 
	
	@Autowired
	private CardHolderRepository cardHolderRepository; 
	
	@RequestMapping(method = RequestMethod.GET, value = "/fill-issuer-database")
	public ResponseEntity<?> fillIssuerDatabase() {
		
		CardHolder cardHolder = new CardHolder(); 
		cardHolder.setName("Pera");
		cardHolderRepository.save(cardHolder); 
		
		IssuedCard issuedCard = new IssuedCard(); 
		issuedCard.setBalance(new BigDecimal("10000.0"));
		issuedCard.setCardHolder(cardHolder);
		issuedCard.setExpiration(new Date(1546210800000L));
		issuedCard.setPan("123456789");
		issuedCard.setSecurityCode("123");
		issuedCard = issuedCardRepository.save(issuedCard); 
		
		
		
		System.out.println("PCC DATABASE FILLED");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}	
}
