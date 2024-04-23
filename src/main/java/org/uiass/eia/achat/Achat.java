package org.uiass.eia.achat;

import org.uiass.eia.crm.Contact;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity(name="Achat")
@Table(name="achat")
public class Achat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="fournisseur_id")
    private Contact fournisseur;

    @OneToMany(mappedBy = "achatObjet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailAchat> detailsAchat;

    private LocalDate dateAchat;

    private double prix;

}
