package tn.esprit.nomprenomclasse3.Domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Vehicule {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   long idVehicule;
   String immatriculation;
   String marque;
   String modele;

   @Enumerated(value = EnumType.STRING)
   CategorieVehicule categorie;

   BigDecimal tarifJournalier;

   @Enumerated(value = EnumType.STRING)
   StatutVehicule statut;

   @ManyToOne(fetch = FetchType.LAZY)
   @ToString.Exclude
   Agence agence;

   @OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
   @ToString.Exclude
   List<Maintenance> maintenances = new ArrayList<>();

   @OneToMany(mappedBy = "vehicule", fetch = FetchType.LAZY)
   @ToString.Exclude
   List<Reservation> reservations = new ArrayList<>();

   @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(
           name = "vehicule_equipement",
           joinColumns = @JoinColumn(name = "vehicule_id"),
           inverseJoinColumns = @JoinColumn(name = "equipement_id")
   )
   @ToString.Exclude
   Set<Equipement> equipements = new HashSet<>();
}