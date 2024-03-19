package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.ClienteDto;
import com.myproject.auth.caffetteriaremastered.model.Cliente;
import com.myproject.auth.caffetteriaremastered.repository.ClienteRepository;
import com.myproject.auth.caffetteriaremastered.userRole.UtenteGenere;
import jakarta.persistence.criteria.Expression;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.YearMonth;
import java.util.Map;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired
    private ClienteRepository clienteRepository;
    @Override
    public Cliente save(@Valid ClienteDto clienteDto) {
        validateClienteDto(clienteDto);

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDto.getNome());
        cliente.setCognome(clienteDto.getCognome());
        cliente.setNascita((Date) clienteDto.getNascita());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setGenere(UtenteGenere.valueOf(clienteDto.getGenere()));


        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente findById(Long id) {
        Optional<Cliente> result = clienteRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        else{
            throw new IllegalArgumentException("Cliente non trovato con id:" + id);
        }
    }

    @Override
    public Page<Cliente> getClienteByYearMonth(int month, int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByNascita(month, year, pageable);
    }

    @Override
    public Page<Cliente> getByGenere(String genere, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByGenere(UtenteGenere.valueOf(genere),  pageable);
    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente update(Long id, ClienteDto clienteDto) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            if (clienteDto.getNome() != null) {
                cliente.setNome(clienteDto.getNome());
            }
            if (clienteDto.getCognome() != null) {
                cliente.setCognome(clienteDto.getCognome());
            }
            if (clienteDto.getEmail() != null) {
                cliente.setEmail(clienteDto.getEmail());
            }
            if (clienteDto.getNascita() != null) {
                cliente.setNascita((Date) clienteDto.getNascita());
            }
            if (clienteDto.getGenere() != null) {
                cliente.setGenere(UtenteGenere.valueOf(clienteDto.getGenere()));
            }
            return clienteRepository.save(cliente);
        } else {
            throw new IllegalArgumentException("Cliente non trovato con ID: " + id);
        }
    }


    private void validateClienteDto(@NotNull ClienteDto clienteDto) {
        if (!StringUtils.hasLength(clienteDto.getNome())) {
            throw new IllegalArgumentException("Il campo 'nome' non può essere vuoto");
        }
        if (!StringUtils.hasLength(clienteDto.getCognome())) {
            throw new IllegalArgumentException("Il campo 'cognome' non può essere vuoto");
        }
        if (!StringUtils.hasLength(clienteDto.getEmail())) {
            throw new IllegalArgumentException("Il campo 'email' non può essere vuoto");
        }
        if (!StringUtils.hasLength(clienteDto.getNascita().toString())) {
            throw new IllegalArgumentException("Il campo 'data di nascita' non può essere vuoto");
        }
        if (!StringUtils.hasLength(clienteDto.getGenere())) {
            throw new IllegalArgumentException("Il campo 'sesso' non può essere vuoto");
        }
    }

    @Override
    public Page<Cliente> getClienteByYear(int year, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByAnno(year, pageable);
    }

    @Override
    public Page<Cliente> getClienteByInitial(String initial, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByInitial(initial, pageable);
    }

    @Override
    public Page<Cliente> getByEmailDomain(String emailDomain, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findByEmailDomain(emailDomain, pageable);
    }
    @Override
    public Page<Cliente> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder) {

        Specification<Cliente> spec = Specification.where(null);

        if (filters.containsKey("yearMonth")) {
            YearMonth yearMonth = (YearMonth) filters.get("yearMonth");
            spec = spec.and((root, query, cb) -> {
                Expression<YearMonth> nascita = root.get("nascita").as(YearMonth.class);
                return cb.equal(nascita, yearMonth);
            });
        }
        if (filters.containsKey("genere")) {
            UtenteGenere genere = (UtenteGenere) filters.get("genere");
            spec = spec.and((root, query, cb) -> {
                Expression<UtenteGenere> genereExpression = root.get("genere");
                return cb.equal(genereExpression, genere);
            });
        }
        if (filters.containsKey("year")) {
            int year = (int) filters.get("year");
            spec = spec.and((root, query, cb) -> {
                Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("nascita"));
                return cb.equal(yearExpression, year);
            });
        }
        if (filters.containsKey("initial")) {
            String initial = (String) filters.get("initial");
            spec = spec.and((root, query, cb) -> {
                Expression<String> nomeExpression = root.get("nome");
                return cb.like(nomeExpression, initial + "%");
            });
        }

        sortBy = (String) filters.get("sortBy");
        sortOrder = (String) filters.get("sortOrder");


        if (sortBy != null && sortOrder != null) {

            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return clienteRepository.findAll(spec, pageable);
        }

        Pageable pageable = PageRequest.of(page, size);
        return clienteRepository.findAll(spec, pageable);
    }
}
