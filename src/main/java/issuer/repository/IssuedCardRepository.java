package issuer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import issuer.bean.IssuedCard;

@Repository
public interface IssuedCardRepository  extends JpaRepository<IssuedCard, Long>{
	
	IssuedCard findByPan(String pan);
}