package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.ClienteDto;
import com.myproject.auth.caffetteriaremastered.model.Cliente;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ClienteService {

    Cliente save(ClienteDto clienteDto);

    Cliente findById(Long id);

    Page<Cliente> getClienteByYearMonth(int month, int year, int page, int size);

    Page<Cliente> getClienteByYear(int year, int page, int size);

    Page<Cliente> getByGenere(String genere, int page, int size);

    void delete(Long id);

    Cliente update(Long id, ClienteDto clienteDto);

    Page<Cliente> getClienteByInitial(String initial, int page, int size);

    Page<Cliente> getByEmailDomain(String emailDomain, int page, int size);

    Page<Cliente> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder);
}
