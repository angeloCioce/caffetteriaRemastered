package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.dto.CategoriaDto;
import com.myproject.auth.caffetteriaremastered.dto.ProdottoDto;
import com.myproject.auth.caffetteriaremastered.dto.ProdottoDtoWIthCategorie;
import com.myproject.auth.caffetteriaremastered.model.Categoria;
import com.myproject.auth.caffetteriaremastered.model.CategoriaProdotti;
import com.myproject.auth.caffetteriaremastered.model.Cliente;
import com.myproject.auth.caffetteriaremastered.model.Prodotto;
import com.myproject.auth.caffetteriaremastered.repository.CategoriaProdottoRepository;
import com.myproject.auth.caffetteriaremastered.repository.CategoriaRepository;
import com.myproject.auth.caffetteriaremastered.repository.ProdottoRepository;
import com.myproject.auth.caffetteriaremastered.userRole.UtenteGenere;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProdottoServiceImpl implements ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private CategoriaProdottoRepository categoriaProdottoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public Prodotto save(ProdottoDto prodottoDto) {

        Prodotto prodotto = Prodotto.builder()
                .nome_prodotto(prodottoDto.getNome_prodotto())
                .descrizione(prodottoDto.getDescrizione())
                .prezzoIngrosso(prodottoDto.getPrezzoIngrosso())
                .prezzoDettaglio(prodottoDto.getPrezzoDettaglio())
                .quantita(prodottoDto.getQuantita()).build();

        prodottoRepository.save(prodotto);
        List<CategoriaProdotti> categoriaProdottiList = new ArrayList<>();

            for (CategoriaDto categoriaDto : prodottoDto.getCategoria())
            {
                Categoria categoria = categoriaRepository.findById(categoriaDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Categoria non trovata"));

                CategoriaProdotti categoriaProdotti = new CategoriaProdotti();
                categoriaProdotti.setCategoria(categoria);
                categoriaProdotti.setProdotto(prodotto);

                categoriaProdottoRepository.save(categoriaProdotti);
                categoriaProdottiList.add(categoriaProdotti);
            }

        if(categoriaProdottiList.isEmpty()){
            throw new IllegalArgumentException("Nessuna categoria valida specificata nell'aggiunta del prodotto.");
        }

        return prodotto;
    }

    @Override
    public ProdottoDtoWIthCategorie findById(Long id) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + id));

        List<Categoria> categorie = categoriaRepository.findCategorieByProdottoId(id);

        ProdottoDtoWIthCategorie prodottoDto = new ProdottoDtoWIthCategorie();
        prodottoDto.setId_prodotto(prodotto.getId_prodotto());
        prodottoDto.setNome_prodotto(prodotto.getNome_prodotto());
        prodottoDto.setDescrizione(prodotto.getDescrizione());
        prodottoDto.setPrezzo_ingrosso(prodotto.getPrezzoIngrosso());
        prodottoDto.setPrezzo_dettaglio(prodotto.getPrezzoDettaglio());
        prodottoDto.setQuantita(prodotto.getQuantita());
        List<String> nomiCategorie = categorie.stream()
                .map(Categoria::getNome)
                .collect(Collectors.toList());
        prodottoDto.setCategorie(nomiCategorie);

        return prodottoDto;
    }


        @Override
        public void delete (Long id){
            prodottoRepository.deleteById(id);
        }

    @Override
    @Transactional
    public Prodotto update(Long id, ProdottoDto prodottoDto, List<Long> idCategorie) {
        Optional<Prodotto> prodottoOptional = prodottoRepository.findById(id);
        if (prodottoOptional.isPresent()) {
            Prodotto prodotto = prodottoOptional.get();

            if (prodottoDto.getNome_prodotto() != null) {
                prodotto.setNome_prodotto(prodottoDto.getNome_prodotto());
            }
            if (prodottoDto.getDescrizione() != null) {
                prodotto.setDescrizione(prodottoDto.getDescrizione());
            }
            if (prodottoDto.getQuantita() != null) {
                prodotto.setQuantita(prodottoDto.getQuantita());
            }
            if (prodottoDto.getPrezzoIngrosso() != null) {
                prodotto.setPrezzoIngrosso(prodottoDto.getPrezzoIngrosso());
            }
            if (prodottoDto.getPrezzoDettaglio() != null) {
                prodotto.setPrezzoDettaglio(prodottoDto.getPrezzoDettaglio());
            }

            if (idCategorie != null && !idCategorie.isEmpty()) {
                categoriaProdottoRepository.deleteByProdottoId(id);

                for (Long idCategoria : idCategorie) {
                    Categoria categoria = categoriaRepository.findById(idCategoria)
                            .orElseThrow(() -> new IllegalArgumentException("Categoria non trovata con ID: " + idCategoria));

                    CategoriaProdotti categoriaProdotti = new CategoriaProdotti();
                    categoriaProdotti.setProdotto(prodotto);
                    categoriaProdotti.setCategoria(categoria);
                    prodotto.getCategoriaProdotti().add(categoriaProdotti);
                    categoriaProdottoRepository.save(categoriaProdotti);
                }
            }

            return prodottoRepository.save(prodotto);
        } else {
            throw new IllegalArgumentException("Prodotto non trovato con ID: " + id);
        }
    }

    @Override
    public Page<Prodotto> getProdottoByCategoria(String categoria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prodottoRepository.findByCategoria(categoria, pageable);
    }

    @Override
    public Page<Prodotto> getProdottoByInitial(String initial, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prodottoRepository.findByInitial(initial, pageable);
    }

    @Override
    public Page<Prodotto> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder) {

        Specification<Prodotto> spec = Specification.where(null);

        if (filters.containsKey("categoria")) {
            String categoria = (String) filters.get("categoria");
            spec = spec.and((root, query, cb) -> {
                Join<Prodotto, CategoriaProdotti> joinCategoriaProdotti = root.join("categoriaProdotti", JoinType.INNER);
                Join<CategoriaProdotti, Categoria> joinCategoria = joinCategoriaProdotti.join("categoria", JoinType.INNER);
                return cb.equal(joinCategoria.get("nome"), categoria);
            });
        }
        if (filters.containsKey("initial")) {
            String initial = (String) filters.get("initial");
            spec = spec.and((root, query, cb) -> {
                Expression<String> nomeExpression = root.get("nome_prodotto");
                return cb.like(nomeExpression, initial + "%");
            });
        }

        if (filters.containsKey("prezzoIngrosso")) {
            String prezzoIngrossoFilter = (String) filters.get("prezzoIngrosso");
            Double prezzoIngrosso = Double.parseDouble(prezzoIngrossoFilter);
            spec = spec.and(hasPrezzoIngrosso(prezzoIngrosso));
        }

        if (filters.containsKey("prezzoDettaglio")) {
            String prezzoDettaglioFilter = (String) filters.get("prezzoDettaglio");
            Double prezzoDettaglio = Double.parseDouble(prezzoDettaglioFilter);
            spec = spec.and(hasPrezzoDettaglio(prezzoDettaglio));
        }

        if (filters.containsKey("quantita")) {
            String quantitaFilter = (String) filters.get("quantita");
            Integer quantita = Integer.parseInt(quantitaFilter);
            spec = spec.and(hasQuantita(quantita));
        }

        sortBy = (String) filters.get("sortBy");
        sortOrder = (String) filters.get("sortOrder");

        if (sortBy != null && sortOrder != null) {
            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            return prodottoRepository.findAll(spec, pageable);
        }

        Pageable pageable = PageRequest.of(page, size);
        return prodottoRepository.findAll(spec, pageable);

    }

    private Specification<Prodotto> hasPrezzoIngrosso(Double prezzoIngrosso) {
        return (root, query, cb) -> cb.equal(root.get("prezzoIngrosso"), prezzoIngrosso);
    }

    private Specification<Prodotto> hasPrezzoDettaglio(Double prezzoDettaglio) {
        return (root, query, cb) -> cb.equal(root.get("prezzoDettaglio"), prezzoDettaglio);
    }

    private Specification<Prodotto> hasQuantita(Integer quantita) {
        return (root, query, cb) -> cb.equal(root.get("quantita"), quantita);
    }

}
