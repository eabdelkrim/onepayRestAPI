# onepay-kata
# Exercice OnePay - Français - Version 2.0

## Enoncé

Développer une application Spring Boot proposant une API RESTful permettant de faire des paiements.
Cette API devra permettre de sauvegarder, lire, modifier dans une base de données H2 des paiements, tels que:

- Un paiement est représenté par un objet "Transaction" avec les champs:
  - montant: de type BigDecimal
  - type de moyen de paiement: valeur parmi CREDIT_CARD, GIFT_CARD, PAYPAL
  - statut: valeur parmi NEW, AUTHORIZED, CAPTURED
  - commande: liste de lignes de commande, une ligne de commande étant représentée par un objet ayant les champs:
    - nom de produit: chaine de caractères
    - quantité: entier
    - prix: BigDecimal

### Règles de gestion:
- une nouvelle transaction doit se trouver dans l'état "NEW"
- il n'est pas possible de passer le statut de la transaction à "CAPTURED" si la transaction n'est pas dans l'état "AUTHORIZED"
- il n'est pas possible de modifier le statut d'une transaction "CAPTURED"
- il n'est pas possible de modifier la commande lors de la modification d'une transaction


L'API devra être développée en anglais.

Le livrable devra correspondre à un projet accessible sur un repository git public (github/gitlab ou assimilé).

## Validation

Afin de valider fonctionnellement l'API, produire un jeu de test représentant le scénario suivant:
- création d'une transaction d'un montant de 54,80 EUR avec CREDIT_CARD et une commande contenant 4 paires de gants de ski à 10 EUR pièce, et 1 bonnet en laine à 14,80EUR
- modification de la transaction en passant le statut à AUTHORIZED
- modification de la transaction en passant le statut à CAPTURED
- création d'une transaction d'un montant de 208 EUR avec PAYPAL et une commande contenant 1 vélo à 208 EUR
- récupérer toutes les transactions


Vous pouvez poser toute question que vous jugez nécessaire, cela fait partie de l'exercice.
