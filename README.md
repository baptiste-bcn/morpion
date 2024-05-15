# Morpion Solitaire en Android

## Description
Le Morpion Solitaire est un jeu à un seul joueur qui se joue traditionnellement sur une feuille à carreaux. Dans notre version Android, vous pouvez jouer directement sur votre appareil mobile. La position de départ est une croix grecque constituée de 36 intersections.

## Règles du jeu
- À chaque tour, tracez une ligne (horizontale, verticale ou diagonale) reliant quatre intersections marquées à une intersection non marquée.
- La nouvelle intersection devient alors marquée.
- Une ligne ne peut avoir qu'une seule intersection en commun avec une ligne précédemment tracée.
- La partie s'arrête lorsque aucun coup n'est plus possible.
- Le score est le nombre de coups joués; le record actuel est de 178 coups.

## Fonctionnalités
1. **Menu Principal :**
   - Démarrer une partie.
   - Accéder aux options.

2. **Jeu :**
   - Affichage et interaction avec la feuille de jeu, possibilité de faire défiler l'écran.
   - Recentrez facilement la vue sur la croix de départ.
   - Tracez des lignes en un seul geste; les coups illégaux sont ignorés.
   - Le score est affiché en permanence.

3. **Fin de la partie :**
   - Détection automatique de la fin de la partie.
   - Possibilité de faire défiler la feuille post-partie tout en affichant le score.

4. **Gestion des retours :**
   - Un retour en arrière depuis l'activité de jeu ramène au menu.
   - Un retour en arrière depuis le menu ferme l'application.
   - Les parties en cours sont sauvegardées et restaurées lors de la mise en pause de l'application.

5. **Options :**
   - Configuration pour démarrer avec une croix de 24 intersections.
   - Option pour interdire de prolonger une ligne existante.

## Évaluation du Projet
Nous avons obtenu la note de 14/20 lors de ce projet.


---

© Emilien LEDUN, Baptiste BLANCHON & Samet DURAN  
- [Sujet disponible ici](https://iut-fbleau.fr/sitebp/sae4/41_2023/2YENGECE9PN6QNMF.php).
