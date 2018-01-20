package issuer.rest;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import issuer.bean.IssuedCard;
import issuer.bean.StanjeTransakcije;
import issuer.bean.Transakcija;
import issuer.repository.IssuedCardRepository;
import issuer.repository.TransakcijaRepository;

@RestController
@CrossOrigin
@RequestMapping("/issuerMain")
public class IssuerController {

	@Value("${pcc.url}")
	private String pccUrl;
	
	@Value("${error.origin.name}")
	private String errorOriginName; 
	
	@Autowired
	private IssuedCardRepository issuedCardRepository; 

	@Autowired
	private TransakcijaRepository transakcijaRepository; 
	
	private RestTemplate rt = new RestTemplate();

	private final Log logger = LogFactory.getLog(this.getClass()); 			
			
	@RequestMapping(value = "/completePaymentRequest", method = RequestMethod.POST)
	public ResponseEntity<?> completePaymentRequest(@RequestBody CompletePaymentDTO completePaymentDTO) {

//		String url = "http://" + this.pccUrl + "/pccMain/completePaymentResponse";
		Transakcija transakcija = new Transakcija(); 
		
		String pan = completePaymentDTO.getPan(); 
		IssuedCard issuedCard = issuedCardRepository.findByPan(pan);
		
		CompletePaymentResponseDTO completePaymentResponseDTO = new CompletePaymentResponseDTO(); 
		
		if(issuedCard == null) {
			completePaymentResponseDTO.setSuccess(false);
			completePaymentResponseDTO.setErrorInfo("FAILURE");
			completePaymentResponseDTO.setReason("Los PAN broj, nema takve kartice!");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
		}
		
		if(!issuedCard.getSecurityCode().equals(completePaymentDTO.getSecurityCode())) {
			completePaymentResponseDTO.setSuccess(false);
			completePaymentResponseDTO.setErrorInfo("FAILURE");
			completePaymentResponseDTO.setReason("Los security code!");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
		}
		
		if(!issuedCard.getCardHolder().getName().equals(completePaymentDTO.getCardHolderName())) {
			completePaymentResponseDTO.setSuccess(false);
			completePaymentResponseDTO.setErrorInfo("FAILURE");
			completePaymentResponseDTO.setReason("Los card holder name!");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
		}
		
		int mesecIsteka = issuedCard.getExpiration().getMonth() + 1; 
		int godinaIsteka = issuedCard.getExpiration().getYear() + 1900; 
		int trenutniMesec = (new Date()).getMonth() + 1; 
		int trenutnaGodina = (new Date()).getYear() + 1900; 
		
		if(trenutnaGodina>godinaIsteka){
			completePaymentResponseDTO.setSuccess(false);
			completePaymentResponseDTO.setErrorInfo("FAILURE");
			completePaymentResponseDTO.setReason("Istekla kartica!");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
		}
		else if(trenutnaGodina==godinaIsteka){
			if(trenutniMesec>mesecIsteka) {
				completePaymentResponseDTO.setSuccess(false);
				completePaymentResponseDTO.setErrorInfo("FAILURE");
				completePaymentResponseDTO.setReason("Istekla kartica!");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
			}
		}
		
		////////////////////////////////////////////////
		// prosla autentifiakcija i autorizacija valjda
		////////////////////////////////////////////////
		BigDecimal racun = issuedCard.getBalance().subtract(completePaymentDTO.getAmount()); 
		
		
		System.out.println();
		if(racun.compareTo(BigDecimal.ZERO) < 0)
		{
			completePaymentResponseDTO.setSuccess(false);
			completePaymentResponseDTO.setErrorInfo("FAILURE");
			completePaymentResponseDTO.setReason("Nema dovoljno sredstava na racunu!");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(completePaymentResponseDTO);
		}
		else {
			issuedCard.setBalance(racun);
		}
		
		transakcija.setAcquirerTimestamp(completePaymentDTO.getAcquirerTimestamp());
		transakcija.setAmount(completePaymentDTO.getAmount());
		transakcija.setIssuedCard(issuedCard);
		transakcija.setTransactionIdAquirer(completePaymentDTO.getAcquirerOrderId());
		transakcija.setVreme(new Date());
		transakcija.setStanje(StanjeTransakcije.Zapoceta);
		transakcija = transakcijaRepository.save(transakcija);
		
		completePaymentResponseDTO.setAcquirerOrderId(completePaymentDTO.getAcquirerOrderId());
		completePaymentResponseDTO.setAcquirerTimestamp(completePaymentDTO.getAcquirerTimestamp());
		completePaymentResponseDTO.setIssuerTransactionId(transakcija.getId());
		completePaymentResponseDTO.setIssuerTimestamp(new Date());
		completePaymentResponseDTO.setSuccess(true);

		return ResponseEntity.ok(completePaymentResponseDTO); 

	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<?> exceptionHandlerHttpError(HttpClientErrorException ex) {
		String body = ex.getResponseBodyAsString();
		RestClientExceptionInfo info = new RestClientExceptionInfo(); 
		
		
		if(RestClientExceptionInfo.parse(body) == null) {
			//ova aplikacija je uzrok exceptiona
			//priprema se exception za propagiranje dalje i loguje se
			info.setOrigin(errorOriginName);
			info.setInfo(body);
		}
		else {
			info.setOrigin(RestClientExceptionInfo.parse(body).getOrigin() );
			info.setInfo(RestClientExceptionInfo.parse(body).getInfo() );
		}
		logger.error("HttpClientErrorException, info:" + RestClientExceptionInfo.toJSON(info));
		
		
		return ResponseEntity.status(ex.getStatusCode()).body(RestClientExceptionInfo.toJSON(info));
	}

}
