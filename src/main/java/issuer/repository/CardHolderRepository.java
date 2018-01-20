package issuer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import issuer.bean.CardHolder;

@Repository
public interface CardHolderRepository  extends JpaRepository<CardHolder, Long>{
	
}