# Stratégie de fetch et cascade JPA

## 1. Contrat -> Paiement
- Côté propriétaire : `Paiement.contrat`
- `@ManyToOne(fetch = FetchType.LAZY)`
- `@OneToMany(mappedBy = "contrat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)`
- Justification : un paiement n'a de sens que rattaché à un contrat. La suppression du contrat doit supprimer ses paiements, et retirer un paiement de la collection du contrat doit aussi le supprimer en base.

## 2. Agence -> Vehicule
- Côté propriétaire : `Vehicule.agence`
- `@ManyToOne(fetch = FetchType.LAZY)` côté `Vehicule`
- `@OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)` côté `Agence`
- Justification : un véhicule appartient à une agence, mais il reste cohérent si cette agence est supprimée ; il ne faut donc pas propager la suppression de l'agence vers les véhicules.

## 3. Agence -> Employe
- Côté propriétaire : `Employe.agence`
- `@ManyToOne(fetch = FetchType.LAZY)` côté `Employe`
- `@OneToMany(mappedBy = "agence", fetch = FetchType.LAZY)` côté `Agence`
- Justification : un employé est rattaché à son agence, sans dépendance forte. La suppression de l'agence ne doit pas supprimer tous les employés.

## 4. Vehicule <-> Equipement
- Côté propriétaire : `Vehicule.equipements`
- `@ManyToMany(fetch = FetchType.LAZY) @JoinTable(name = "vehicule_equipement")`
- `@ManyToMany(mappedBy = "equipements", fetch = FetchType.LAZY)` côté `Equipement`
- Justification : un équipement peut être partagé entre plusieurs véhicules ; la relation est un objet de jointure, sans cascade de suppression.

## 5. Client -> Reservation
- Côté propriétaire : `Reservation.client`
- `@ManyToOne(fetch = FetchType.LAZY)` côté `Reservation`
- `@OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)` côté `Client`
- Justification : les réservations sont créées avec le client, mais elles restent distinctes. On propage uniquement `PERSIST` et non toute la suppression.

## 6. Reservation -> Vehicule
- Côté propriétaire : `Reservation.vehicule`
- `@ManyToOne(fetch = FetchType.LAZY)`
- Justification : la réservation dépend d'un véhicule, mais le véhicule n'est pas supprimé en cascade lors de la suppression d'une réservation.

## 7. Reservation <-> Contrat
- Côté propriétaire : `Contrat.reservation`
- `@OneToOne(fetch = FetchType.LAZY)` côté `Contrat`
- `@OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)` côté `Reservation`
- Justification : un contrat n'a de sens que s'il est lié à une réservation. La cascade alimente le cycle de vie complet des deux côtés et la suppression d'une relation supprime aussi l'entité liée.

## 8. Vehicule -> Maintenance
- Côté propriétaire : `Maintenance.vehicule`
- `@ManyToOne(fetch = FetchType.LAZY)` côté `Maintenance`
- `@OneToMany(mappedBy = "vehicule", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)` côté `Vehicule`
- Justification : une maintenance est créée pour un véhicule, sans dépendance telle qu'une suppression de véhicule entraînerait la suppression de ses historiques de maintenance.

## Conclusion
- Le choix par défaut est `FetchType.LAZY` pour éviter les chargements inutiles.
- Les cascades ne sont utilisées que lorsque l'enfant n'a pas de sens sans son parent : `Contrat -> Paiement`, `Reservation -> Contrat`, et `Vehicule -> Maintenance` en `PERSIST` uniquement.
- L'usage de `orphanRemoval` sur les compositions fortes garantit la cohérence des données sans impacter les relations de type référence simple.
