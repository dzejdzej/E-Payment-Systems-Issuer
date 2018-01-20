package issuer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import issuer.bean.Transakcija;

@Repository
public interface TransakcijaRepository  extends JpaRepository<Transakcija, Long>{
	
}