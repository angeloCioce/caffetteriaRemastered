package com.myproject.auth.caffetteriaremastered.repository;

import com.myproject.auth.caffetteriaremastered.model.Cliente;
import com.myproject.auth.caffetteriaremastered.userRole.UtenteGenere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByGenere(UtenteGenere genere, Pageable pageable);
    @Query("SELECT c FROM Cliente c WHERE EXTRACT(MONTH FROM c.nascita) = :month AND EXTRACT(YEAR FROM c.nascita) = :year")
    Page<Cliente> findByNascita(int month, int year, Pageable pageable);
    @Query("SELECT c FROM Cliente c WHERE YEAR(c.nascita) = :year")
    Page<Cliente> findByAnno(@Param("year") int year, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.nome LIKE :initial%")
    Page<Cliente> findByInitial(@Param("initial") String initial, Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE c.email LIKE %:emailDomain%")
    Page<Cliente> findByEmailDomain(@Param("emailDomain") String emailDomain, Pageable pageable);

    Page<Cliente> findAll(Specification<Cliente> spec, Pageable pageable);
}
