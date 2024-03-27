package com.myproject.auth.caffetteriaremastered.controller;

import com.myproject.auth.caffetteriaremastered.dto.ClienteDto;
import com.myproject.auth.caffetteriaremastered.dto.FilterRequestClienti;
import com.myproject.auth.caffetteriaremastered.model.Cliente;
import com.myproject.auth.caffetteriaremastered.service.ClienteService;
import com.myproject.auth.caffetteriaremastered.userRole.UtenteGenere;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/addCliente")
    public ResponseEntity<ClienteDto> saveNewCliente(@RequestBody ClienteDto clienteDto)
    {
        clienteService.save(clienteDto);
        return new ResponseEntity<>(clienteDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @GetMapping("/getCliente/{id_cliente}")
    public ResponseEntity<ClienteDto> findClienteById(@PathVariable("id_cliente") Long id)
    {
        Cliente cliente = clienteService.findById(id);
        ClienteDto clienteResponse = modelMapper.map(cliente, ClienteDto.class);
        return ResponseEntity.ok().body(clienteResponse);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @DeleteMapping("/deleteCliente/{id_cliente}")
    public ResponseEntity<String> deleteClienteById(@PathVariable("id_cliente") Long id)
    {
        clienteService.delete(id);
        String apiResponse = ("Record deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/filtri")
    public ResponseEntity<?> filterClienti(@Validated @RequestBody FilterRequestClienti filterRequest)
    {
        String type = filterRequest.getType();
        int page = filterRequest.getPage();
        int size = filterRequest.getSize();

        switch (type) {
            case "yearMonth":
                int month = filterRequest.getMonth();
                int year = filterRequest.getYear();
                Page<Cliente> clienteByYearMonth = clienteService.getClienteByYearMonth(month, year, page, size);
                if (clienteByYearMonth.isEmpty()) {
                    return new ResponseEntity<>("Non ci sono clienti nati nell'anno e/o mese specificato", HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(clienteByYearMonth, HttpStatus.OK);
            case "genere":
                String genere = filterRequest.getGenere();
                try {
                    Page<Cliente> clientiByGenere = clienteService.getByGenere(genere, page, size);
                    if (clientiByGenere.isEmpty()) {
                        return new ResponseEntity<>("Non ci sono clienti con il genere specificato", HttpStatus.NOT_FOUND);
                    }
                    return new ResponseEntity<>(clientiByGenere, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>("Il genere specificato non è valido", HttpStatus.BAD_REQUEST);
                }
            case "year":
                int yearFilter = filterRequest.getYear();
                Page<Cliente> clientiByYear = clienteService.getClienteByYear(yearFilter, page, size);
                if (clientiByYear.isEmpty()) {
                    return new ResponseEntity<>("Non ci sono clienti nati nell'anno specificato", HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(clientiByYear, HttpStatus.OK);
            case "initial":
                String initial = filterRequest.getInitial();
                Page<Cliente> clientiByInitial = clienteService.getClienteByInitial(initial, page, size);
                if (clientiByInitial.isEmpty()) {
                    return new ResponseEntity<>("Non ci sono clienti che iniziano con la lettera '" + initial + "'", HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(clientiByInitial, HttpStatus.OK);
            case "emailDomain":
                String emailDomain = filterRequest.getEmailDomain();
                Page<Cliente> clientiByEmailDomain = clienteService.getByEmailDomain(emailDomain, page, size);
                if (clientiByEmailDomain.isEmpty()) {
                    return new ResponseEntity<>("Non ci sono clienti con l'email che utilizza il dominio specificato", HttpStatus.NOT_FOUND);
                }
                return new ResponseEntity<>(clientiByEmailDomain, HttpStatus.OK);
            default:
                return new ResponseEntity<>("Tipo di filtro non valido", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PostMapping("/filtri.dinamici")
    public ResponseEntity<?> filterClientiDinamic(@Validated @RequestBody FilterRequestClienti filterRequest) {
        Map<String, Object> filters = new HashMap<>();

        if (filterRequest.getYear() != null) {
            filters.put("year", filterRequest.getYear());
        }

        if (filterRequest.getMonth() != null) {
            filters.put("month", filterRequest.getMonth());
        }

        if (filterRequest.getGenere() != null) {
            filters.put("genere", UtenteGenere.valueOf(filterRequest.getGenere()));
        }

        if (filterRequest.getInitial() != null) {
            filters.put("initial", filterRequest.getInitial());
        }

        if (filterRequest.getEmailDomain() != null) {
            filters.put("emailDomain", filterRequest.getEmailDomain());
        }

        String sortBy = filterRequest.getSortBy();
        String sortOrder = filterRequest.getSortOrder();

        if (sortBy != null && sortOrder != null) {
            filters.put("sortBy", sortBy);
            filters.put("sortOrder", sortOrder);
        }


        int page = filterRequest.getPage();
        int size = filterRequest.getSize();

        Page<Cliente> filteredResults = clienteService.applyFilters(filters, page, size, sortBy, sortOrder);
        return new ResponseEntity<>(filteredResults, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'DIPENDENTE', 'MANUTENTORE')")
    @PatchMapping("/update/{id_cliente}")
    public ResponseEntity<Cliente> patchCliente(@PathVariable("id_cliente") Long id, @Validated @RequestBody ClienteDto clienteDto) {
        Cliente updatedCliente = clienteService.update(id, clienteDto);
        return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
    }



}
