package tn.esprit.nomprenomclasse3.Domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Equipement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idEquipement;
    String libelle;

    @ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)
    @ToString.Exclude
    Set<Vehicule> vehicules = new HashSet<>();
}
