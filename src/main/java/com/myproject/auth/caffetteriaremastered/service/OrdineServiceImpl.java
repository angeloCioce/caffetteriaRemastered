package com.myproject.auth.caffetteriaremastered.service;

import com.myproject.auth.caffetteriaremastered.config.ProdottoMapper;
import com.myproject.auth.caffetteriaremastered.dto.*;
import com.myproject.auth.caffetteriaremastered.model.*;
import com.myproject.auth.caffetteriaremastered.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrdineServiceImpl implements OrdineService{

    @Autowired
    private OrdineRepository ordineRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoOrdiniRepository prodottoOrdiniRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Ordine save(OrdineDto ordineDto) {
        Cliente cliente = clienteRepository.findById(ordineDto.getId_cliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato"));
        Utente utente = utenteRepository.findById(ordineDto.getId_utente())
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        Double prezzoTotale = 0.0;

        Ordine ordine = Ordine.builder()
                .dataOrdine(LocalDateTime.now())
                .prezzoTotale(prezzoTotale)
                .cliente(cliente)
                .utente(utente)
                .build();

        ordineRepository.save(ordine);
        List<Prodotti_Ordini> prodottiOrdiniList = new ArrayList<>();

        for (ProdottoDto prodottoDto : ordineDto.getProdotti()) {
            Prodotto prodotto = prodottoRepository.findById(prodottoDto.getId_prodotto())
                    .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

            Prodotti_Ordini prodottiOrdini = new Prodotti_Ordini();
            prodottiOrdini.setOrdine(ordine);
            prodottiOrdini.setProdotto(prodotto);
            prodottiOrdini.setQuantitaOrdine(prodottoDto.getQuantita());
            prezzoTotale += prodotto.getPrezzoDettaglio() * prodottoDto.getQuantita();

            prodottoOrdiniRepository.save(prodottiOrdini);
            prodottiOrdiniList.add(prodottiOrdini);
        }

        if (prodottiOrdiniList.isEmpty()) {
            throw new IllegalArgumentException("Nessun prodotto valido specificato nell'ordine.");
        }

        ordine.setPrezzoTotale(prezzoTotale);
        ordineRepository.save(ordine);

        return ordine;
    }

    @Override
    public OrdineDto findById(Long id) {
        Ordine ordine = ordineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ordine non trovato con id:" + id));
        return mapToOrdineDtoWithDetails(ordine);
    }

    @Override
    public void delete(Long id) {
        ordineRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Ordine update(Long id, OrdineDto ordineDto, List<IdProdottoQuantita> idProdotti) {
        Optional<Ordine> ordineOptional = ordineRepository.findById(id);
        if (ordineOptional.isPresent())
        {
            Ordine ordine = ordineOptional.get();

            if(ordineDto.getDataOrdine() != null)
            {
                ordine.setDataOrdine(ordineDto.getDataOrdine());
            }
            if(ordineDto.getPrezzoTotale() != null)
            {
                ordine.setPrezzoTotale(ordineDto.getPrezzoTotale());
            }
            if(ordineDto.getId_cliente() != null)
            {
                Cliente cliente = clienteRepository.findById(ordineDto.getId_cliente())
                        .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + ordineDto.getId_cliente()));
                ordine.setCliente(cliente);
            }
            if (ordineDto.getId_utente() != null) {
                Utente utente = utenteRepository.findById(ordineDto.getId_utente())
                        .orElseThrow(() -> new IllegalArgumentException("Utente non trovato con ID: " + ordineDto.getId_utente()));
                ordine.setUtente(utente);
            }

            if (idProdotti != null && !idProdotti.isEmpty()) {
                prodottoOrdiniRepository.deleteByOrdineId(id);
                Set<Prodotti_Ordini> prodottiOrdiniToUpdate = new HashSet<>();

                for (IdProdottoQuantita idProdottoQuantita : idProdotti) {
                    Prodotto prodotto = prodottoRepository.findById(idProdottoQuantita.getId_prodotto())
                            .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato con ID: " + idProdottoQuantita.getId_prodotto()));

                    Optional<Prodotti_Ordini> prodottiOrdiniOptional = prodottoOrdiniRepository.findByOrdineIdAndProdottoId(ordine.getId_ordine(), idProdottoQuantita.getId_prodotto());
                    if (prodottiOrdiniOptional.isPresent()) {
                        Prodotti_Ordini prodottiOrdini = prodottiOrdiniOptional.get();
                        prodottiOrdini.setOrdine(ordine);
                        prodottiOrdini.setProdotto(prodotto);
                        prodottiOrdini.setQuantitaOrdine(idProdottoQuantita.getQuantita_ordine());
                        prodottiOrdiniToUpdate.add(prodottiOrdini);
                        prodottoOrdiniRepository.save(prodottiOrdini);
                    } else {
                        Prodotti_Ordini newProdottiOrdini = new Prodotti_Ordini();
                        newProdottiOrdini.setOrdine(ordine);
                        newProdottiOrdini.setProdotto(prodotto);
                        newProdottiOrdini.setQuantitaOrdine(idProdottoQuantita.getQuantita_ordine());
                        prodottiOrdiniToUpdate.add(newProdottiOrdini);
                        prodottoOrdiniRepository.save(newProdottiOrdini);
                    }
                }
                ordine.setProdottiOrdini(prodottiOrdiniToUpdate);
            }
            return ordineRepository.save(ordine);
                } else {
                     throw new IllegalArgumentException("Prodotto non trovato con ID: " + id);
                }
        }

    private OrdineDto mapToOrdineDtoWithDetails(Ordine ordine) {
        OrdineDto ordineDto = modelMapper.map(ordine, OrdineDto.class);


        List<Prodotti_Ordini> prodottiOrdini = prodottoOrdiniRepository.findByOrdine(ordine);
        List<ProdottoDto> prodottiDto = prodottiOrdini.stream()
                .map(po -> {
                    ProdottoDto prodottoDto = modelMapper.map(po.getProdotto(), ProdottoDto.class);
                    prodottoDto.setQuantita(po.getQuantitaOrdine());
                    prodottoDto.setId_categoria(po.getProdotto().getCategoriaProdotti().stream()
                            .map(categoriaProdotto -> categoriaProdotto.getCategoria().getId())
                            .collect(Collectors.toList()));
                    prodottoDto.setCategoria(po.getProdotto().getCategoriaProdotti().stream()
                            .map(categoriaProdotto -> {
                                CategoriaDto categoriaDto = new CategoriaDto();
                                categoriaDto.setId(categoriaProdotto.getCategoria().getId());
                                categoriaDto.setNome(categoriaProdotto.getCategoria().getNome());
                                return categoriaDto;
                            }).collect(Collectors.toList()));
                    return prodottoDto;
                })
                .collect(Collectors.toList());
        ordineDto.setProdotti(prodottiDto);

        if (ordine.getUtente() != null) {
            ordineDto.setId_utente(ordine.getUtente().getId_utente());
            ordineDto.setUsername_utente(ordine.getUtente().getUsername());
        }

        if (ordine.getCliente() != null) {
            ordineDto.setId_cliente(ordine.getCliente().getId());
            ordineDto.setNome_cliente(ordine.getCliente().getNome());
            ordineDto.setCognome_cliente(ordine.getCliente().getCognome());
        }

        return ordineDto;
    }


    public List<ProdottoPercentualeVendite> calcolaPercentualeProdottiPiuVenduti(Long idOrdine) {
        List<Prodotti_Ordini> prodottiOrdini = prodottoOrdiniRepository.findByOrdineId(idOrdine);

        int quantitaTotaleVenduta = prodottiOrdini.stream()
                .mapToInt(Prodotti_Ordini::getQuantitaOrdine)
                .sum();

        List<ProdottoPercentualeVendite> prodottiPercentuali = prodottiOrdini.stream()
                .collect(Collectors.groupingBy(Prodotti_Ordini::getProdotto, Collectors.summingDouble(p -> (double) p.getQuantitaOrdine())))
                .entrySet().stream()
                .map(entry -> {
                    double percentuale = (entry.getValue() / quantitaTotaleVenduta) * 100;
                    return new ProdottoPercentualeVendite(entry.getKey().getId_prodotto(), percentuale);
                })
                .collect(Collectors.toList());

        prodottiPercentuali.sort(Comparator.comparingDouble(ProdottoPercentualeVendite::getPercentualeVendite).reversed());

        return prodottiPercentuali;
    }

    public List<ProdottoPercentualeVendite> calcolaPercentualeProdottiSuTuttiOrdini() {
        List<Prodotti_Ordini> prodottiOrdini = prodottoOrdiniRepository.findAll();

        int quantitaTotaleVenduta = prodottiOrdini.stream()
                .mapToInt(Prodotti_Ordini::getQuantitaOrdine)
                .sum();

        List<ProdottoPercentualeVendite> prodottiPercentuali = prodottiOrdini.stream()
                .collect(Collectors.groupingBy(Prodotti_Ordini::getProdotto, Collectors.summingDouble(p -> (double) p.getQuantitaOrdine())))
                .entrySet().stream()
                .map(entry -> {
                    double percentuale = (entry.getValue() / quantitaTotaleVenduta) * 100;
                    return new ProdottoPercentualeVendite(entry.getKey().getId_prodotto(), percentuale);
                })
                .collect(Collectors.toList());

        prodottiPercentuali.sort(Comparator.comparingDouble(ProdottoPercentualeVendite::getPercentualeVendite).reversed());

        return prodottiPercentuali;
    }


    public List<CostiSpeseGrafico> calcolaCostiSpeseGrafico() {
        List<CostiSpeseGrafico> costiSpeseGraficoList = new ArrayList<>();

        List<Prodotti_Ordini> prodottiOrdiniList = prodottoOrdiniRepository.findAll();

        for (Prodotti_Ordini prodottiOrdini : prodottiOrdiniList) {
            double prezzoDettaglio = 0.0;
            double prezzoIngrosso = 0.0;

            Prodotto prodotto = prodottiOrdini.getProdotto();
            if (prodotto != null) {
                prezzoDettaglio = prodotto.getPrezzoDettaglio();
                prezzoIngrosso = prodotto.getPrezzoIngrosso();
            }

            double costoTotale = prezzoDettaglio * prodottiOrdini.getQuantitaOrdine();
            double speseNette = prezzoIngrosso * prodottiOrdini.getQuantitaOrdine();
            double guadagni = costoTotale - speseNette;

            Long idOrdine = prodottiOrdini.getOrdine().getId_ordine();
            Long idProdotto = prodottiOrdini.getProdotto().getId_prodotto();

            CostiSpeseGrafico costiSpeseGrafico = new CostiSpeseGrafico(idOrdine, idProdotto, costoTotale, speseNette, guadagni);
            costiSpeseGraficoList.add(costiSpeseGrafico);
        }

        return costiSpeseGraficoList;
    }


    public List<ClientePercentualeOrdini> getClientiPercentualeAcquistoAnno(int anno) {
        List<Ordine> ordiniAnno = ordineRepository.findByDataOrdineYear(anno);
        Map<Long, Integer> conteggioOrdiniPerCliente = new HashMap<>();

        for (Ordine ordine : ordiniAnno) {
            Long clienteId = ordine.getCliente().getId();
            conteggioOrdiniPerCliente.put(clienteId, conteggioOrdiniPerCliente.getOrDefault(clienteId, 0) + 1);
        }

        int totaleOrdini = ordiniAnno.size();
        List<ClientePercentualeOrdini> clientiPercentualeAcquisto = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : conteggioOrdiniPerCliente.entrySet()) {
            Long clienteId = entry.getKey();
            int numeroOrdiniCliente = entry.getValue();
            double percentualeAcquisto = (numeroOrdiniCliente * 100.0) / totaleOrdini;
            clientiPercentualeAcquisto.add(new ClientePercentualeOrdini(clienteId, percentualeAcquisto));
        }

        return clientiPercentualeAcquisto;
    }


    public GuadagnoPerditaTotale calcolaGuadagnoPerditaTotale() {
        double guadagnoTotale = 0.0;
        double perditaTotale = 0.0;

        List<Prodotti_Ordini> prodottiOrdiniList = prodottoOrdiniRepository.findAll();

        for (Prodotti_Ordini prodottiOrdini : prodottiOrdiniList) {
            double prezzoDettaglio = 0.0;
            double prezzoIngrosso = 0.0;

            Prodotto prodotto = prodottiOrdini.getProdotto();
            if (prodotto != null) {
                prezzoDettaglio = prodotto.getPrezzoDettaglio();
                prezzoIngrosso = prodotto.getPrezzoIngrosso();
            }

            double costoTotale = prezzoDettaglio * prodottiOrdini.getQuantitaOrdine();
            double speseNette = prezzoIngrosso * prodottiOrdini.getQuantitaOrdine();
            double guadagni = costoTotale - speseNette;

            guadagnoTotale += guadagni;
            perditaTotale += speseNette;
        }

        return new GuadagnoPerditaTotale(guadagnoTotale, perditaTotale);
    }

    public List<UtentePercentualeVendite> getUtentiPercentualeVendite() {
        List<Utente> utenti = utenteRepository.findAll();
        List<UtentePercentualeVendite> utentiPercentualeVendite = new ArrayList<>();

        Map<Long, Integer> conteggioOrdiniPerUtente = new HashMap<>();

        for (Utente utente : utenti) {
            Long userId = utente.getId_utente();
            int numeroOrdiniPerUtente = ordineRepository.countByUserId(userId);
            conteggioOrdiniPerUtente.put(userId, numeroOrdiniPerUtente);
        }

        int totaleOrdini = conteggioOrdiniPerUtente.values().stream().mapToInt(Integer::intValue).sum();

        for (Utente utente : utenti) {
            Long userId = utente.getId_utente();
            int numeroOrdiniPerUtente = conteggioOrdiniPerUtente.getOrDefault(userId, 0);
            double percentualeVendite = (numeroOrdiniPerUtente * 100.0) / totaleOrdini;
            utentiPercentualeVendite.add(new UtentePercentualeVendite(userId, percentualeVendite));
        }

        return utentiPercentualeVendite;
    }

    @Override
    public Page<?> applyFilters(Map<String, Object> filters, int page, int size, String sortBy, String sortOrder) {
        Specification<Ordine> spec = Specification.where(null);
        Pageable pageable = PageRequest.of(page, size);

        Long idProdotto = (Long) filters.get("idProdotto");
        if (idProdotto != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Ordine, Prodotti_Ordini> prodottiOrdiniJoin = root.join("prodottiOrdini");
                return cb.equal(prodottiOrdiniJoin.get("prodotto").get("id"), idProdotto);
            });
        }

        Long idCliente = (Long) filters.get("idCliente");
        if (idCliente != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Ordine, Cliente> clienteJoin = root.join("cliente");
                return cb.equal(clienteJoin.get("id"), idCliente);
            });
        }

        Long idUtente = (Long) filters.get("idUtente");
        if (idUtente != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Ordine, Utente> utenteJoin = root.join("utente");
                return cb.equal(utenteJoin.get("id"), idUtente);
            });
        }

        if (filters.containsKey("year")) {
            Integer year = (Integer) filters.get("year");
            spec = spec.and((root, query, cb) -> {
                Expression<Integer> yearExpression = cb.function("YEAR", Integer.class, root.get("dataOrdine"));
                return cb.equal(yearExpression, year);
            });
        }

        if (sortBy != null && sortOrder != null && sortBy.equals("quantitaOrdine")) {
            String jpqlQuery = "SELECT o FROM Ordine o JOIN o.prodottiOrdini po WHERE 1 = 1 ";

            if (filters.containsKey("idCliente")) {
                jpqlQuery += "AND o.cliente.id = :idCliente ";
            }

            if (filters.containsKey("year")) {
                jpqlQuery += "AND FUNCTION('YEAR', o.dataOrdine) = :year ";
            }

            if (filters.containsKey("idProdotto")) {
                jpqlQuery += "AND po.prodotto.id = :idProdotto ";
            }

            if (filters.containsKey("idUtente")) {
                jpqlQuery += "AND o.utente.id = :idUtente ";
            }

            jpqlQuery += "GROUP BY o.id ORDER BY SUM(po.quantitaOrdine) " + sortOrder;

            TypedQuery<Ordine> query = entityManager.createQuery(jpqlQuery, Ordine.class);

            if (filters.containsKey("idCliente")) {
                query.setParameter("idCliente", filters.get("idCliente"));
            }

            if (filters.containsKey("year")) {
                query.setParameter("year", filters.get("year"));
            }

            if (filters.containsKey("idProdotto")) {
                query.setParameter("idProdotto", filters.get("idProdotto"));
            }

            if (filters.containsKey("idUtente")) {
                query.setParameter("idUtente", filters.get("idUtente"));
            }

            query.setFirstResult(page * size)
                    .setMaxResults(size);

            List<Ordine> result = query.getResultList();

            long total = (long) entityManager.createQuery("SELECT COUNT(DISTINCT o.id) FROM Ordine o WHERE 1 = 1 ")
                    .getSingleResult();

            return new PageImpl<>(result.stream().map(this::mapToOrdineDto).collect(Collectors.toList()), pageable, total);
        } else {

            Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            spec = spec.and((root, query, cb) -> {
                root.fetch("utente", JoinType.LEFT); // Carica le informazioni sull'utente
                root.fetch("prodottiOrdini", JoinType.LEFT).fetch("prodotto", JoinType.LEFT); // Carica le informazioni sui prodotti
                return null;
            });

            return ordineRepository.findAll(spec, pageable).map(this::mapToOrdineDto);
        }
    }

    private OrdineDto mapToOrdineDto(Ordine ordine) {
        OrdineDto ordineDto = new OrdineDto();
        BeanUtils.copyProperties(ordine, ordineDto);

        ordineDto.setId(ordine.getId_ordine());
        ordineDto.setId_cliente(ordine.getCliente().getId());
        ordineDto.setId_utente(ordine.getUtente().getId_utente());
        ordineDto.setUsername_utente(ordine.getUtente().getUsername());
        ordineDto.setNome_cliente(ordine.getCliente().getNome());
        ordineDto.setCognome_cliente(ordine.getCliente().getCognome());
        if (ordine.getProdottiOrdini() != null && !ordine.getProdottiOrdini().isEmpty()) {
            List<ProdottoDto> prodottiDto = ordine.getProdottiOrdini().stream()
                    .map(prodottiOrdini -> {
                        ProdottoDto prodottoDto = ProdottoMapper.mapToProdottoDto(prodottiOrdini.getProdotto());
                        prodottoDto.setQuantitaOrdine(prodottiOrdini.getQuantitaOrdine());

                        List<CategoriaDto> categoria = prodottiOrdini.getProdotto().getCategoriaProdotti().stream()
                                .map(categoriaProdotto -> {
                                    CategoriaDto categoriaDto = new CategoriaDto();
                                    categoriaDto.setId(categoriaProdotto.getCategoria().getId());
                                    categoriaDto.setNome(categoriaProdotto.getCategoria().getNome());
                                    return categoriaDto;
                                })
                                .collect(Collectors.toList());

                        prodottoDto.setCategoria(categoria);
                        return prodottoDto;
                    })
                    .collect(Collectors.toList());
            ordineDto.setProdotti(prodottiDto);
        }

        return ordineDto;
    }
}
