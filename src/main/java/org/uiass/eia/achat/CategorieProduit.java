package org.uiass.eia.achat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CategorieProduit {

    LIVRES("Livres"),
    BD("BD"),
    EBOOKS("Ebooks"),
    PAPETERIE("Papeterie"),
    MUSIQUE("Musique"),
    CD("CD"),
    VINYLES("Vinyles"),
    JEUX_VIDEO("Jeux vidéo"),
    CONSOLES("Consoles"),
    FILMS("Films"),
    SERIES_TV("Séries TV"),
    DVD("DVD"),
    BLU_RAY("Blu-ray"),
    JEUX("Jeux"),
    JOUETS("Jouets"),
    INFORMATIQUE("Informatique"),
    TABLETTES("Tablettes"),
    SMARTPHONES("Smartphones & Objets connectés"),
    PHOTO("Photo"),
    CAMERAS("Caméras"),
    DRONES("Drones"),
    SON("Son"),
    CASQUES("Casques"),
    ENCEINTES("Enceintes"),
    TV("TV"),
    VIDEO("Vidéo"),
    HOME_CINEMA("Home cinéma"),
    MOBILITE("Mobilité"),
    SPORT("Sport"),
    BAGAGERIE("Bagagerie"),
    ELECTROMENAGER("Électroménager"),
    CUISINE("Cuisine"),
    BEAUTE("Beauté"),
    SANTE("Santé"),
    FORME("Forme"),
    TIRAGES("Tirages et livres photo"),
    MAISON("Maison"),
    DECORATION("Décoration"),
    BRICOLAGE("Bricolage"),
    JARDIN("Jardin");

    private final String categorieProduit;

    CategorieProduit(String categorieProduit) {
        this.categorieProduit = categorieProduit;
    }

    public String getCategorieProduit() {
        return categorieProduit;
    }

    public static List<String> getAllCategories() {
        return Arrays.stream(CategorieProduit.values())
                .map(CategorieProduit::getCategorieProduit)
                .collect(Collectors.toList());
    }

}
