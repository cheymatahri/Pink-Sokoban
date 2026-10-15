import java.util.Scanner;

public class Sokoban {

    // ==================== DONNÉES GLOBALES ====================
    static final char MUR      = '#';
    static final char LIBRE    = ' ';
    static final char JOUEUR   = '@';
    static final char CAISSE   = '$';
    static final char OBJECTIF = '.';
    static final char CAISSE_OK= '*';

    static final String[][] NIVEAUX = {
            { // Niveau 1
                    "##########",
                    "#        #",
                    "# @$ .   #",
                    "#        #",
                    "#        #",
                    "#        #",
                    "##########"
            }
    };

    // ==================== MAIN ====================
    public static void main(String[] args) {
        menu();
    }

    // ==================== PARTIE AXELLE — MENU ====================
    static void menu() {
        Scanner sc = new Scanner(System.in);
        int choix = 0;

        while (choix != 8) {
            System.out.println("======== SOKOBAN ========");
            System.out.println("1. Jouer au niveau 1");
            System.out.println("2. Jouer au niveau 2");
            System.out.println("3. Jouer au niveau 3");
            System.out.println("4. Résoudre automatiquement (DFS)");
            System.out.println("5. Résoudre automatiquement (BFS)");
            System.out.println("6. Comparer DFS / BFS");
            System.out.println("7. Afficher les règles");
            System.out.println("8. Quitter");
            System.out.print("Votre choix : ");

            if (sc.hasNextInt()) {
                choix = sc.nextInt();
            } else {
                sc.next();
                choix = 0;
            }

            char[][] grille;
            switch (choix) {
                case 1:
                    grille = chargerNiveau(1);
                    afficherGrille(grille);
                    break;
                case 2:
                    grille = chargerNiveau(2);
                    afficherGrille(grille);
                    break;
                case 3:
                    grille = chargerNiveau(3);
                    afficherGrille(grille);
                    break;
                case 4:
                    grille = chargerNiveau(1);
                    resoudreDFS(grille);
                    break;
                case 5:
                    grille = chargerNiveau(1);
                    resoudreBFS(grille);
                    break;
                case 6:
                    grille = chargerNiveau(1);
                    comparerDFS_BFS(grille);
                    break;
                case 7:
                    System.out.println("Règles :");
                    System.out.println("- Déplacez le joueur avec z/s/q/d");
                    System.out.println("- Poussez les caisses $ sur les objectifs .");
                    System.out.println("- Tapez x pour quitter une partie");
                    break;
                case 8:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }

        sc.close();
    }

    // ==================== PARTIE CHEYMA — JEU ====================
    static void afficherGrille(char[][] grille) {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                System.out.print(grille[i][j]);
            }
            System.out.println();
        }
    }

    static char[][] chargerNiveau(int numero) {
        String[] niveau = NIVEAUX[numero - 1];
        char[][] grille = new char[niveau.length][];
        for (int i = 0; i < niveau.length; i++) {
            grille[i] = niveau[i].toCharArray();
        }
        return grille;
    }

    static boolean deplacementValide(char[][] grille, int x, int y, char dir) {
        int nx = x, ny = y;
        switch (dir) {
            case 'z': nx = x - 1; break;
            case 's': nx = x + 1; break;
            case 'q': ny = y - 1; break;
            case 'd': ny = y + 1; break;
            default: return false;
        }
        if (nx < 0 || nx >= grille.length || ny < 0 || ny >= grille[0].length) return false;
        if (grille[nx][ny] == MUR) return false;
        if (grille[nx][ny] == CAISSE || grille[nx][ny] == CAISSE_OK) {
            return peutPousser(grille, x, y, dir);
        }
        return true;
    }

    static boolean peutPousser(char[][] grille, int x, int y, char dir) {
        int cx = x, cy = y;
        switch (dir) {
            case 'z': cx = x - 1; break;
            case 's': cx = x + 1; break;
            case 'q': cy = y - 1; break;
            case 'd': cy = y + 1; break;
            default: return false;
        }
        int dx = cx, dy = cy;
        switch (dir) {
            case 'z': dx = cx - 1; break;
            case 's': dx = cx + 1; break;
            case 'q': dy = cy - 1; break;
            case 'd': dy = cy + 1; break;
        }
        if (dx < 0 || dx >= grille.length || dy < 0 || dy >= grille[0].length) return false;
        return grille[dx][dy] != MUR && grille[dx][dy] != CAISSE && grille[dx][dy] != CAISSE_OK;
    }

    static void deplacerCaisse(char[][] grille, int x, int y, char dir) {
        int cx = x, cy = y;
        switch (dir) {
            case 'z': cx = x - 1; break;
            case 's': cx = x + 1; break;
            case 'q': cy = y - 1; break;
            case 'd': cy = y + 1; break;
        }
        int dx = cx, dy = cy;
        switch (dir) {
            case 'z': dx = cx - 1; break;
            case 's': dx = cx + 1; break;
            case 'q': dy = cy - 1; break;
            case 'd': dy = cy + 1; break;
        }
        if (grille[dx][dy] == OBJECTIF) {
            grille[dx][dy] = CAISSE_OK;
        } else {
            grille[dx][dy] = CAISSE;
        }
        if (grille[cx][cy] == CAISSE_OK) {
            grille[cx][cy] = OBJECTIF;
        } else {
            grille[cx][cy] = LIBRE;
        }
    }

    static void deplacerJoueur(char[][] grille, char dir) {
        int x = -1, y = -1;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == JOUEUR) {
                    x = i; y = j;
                }
            }
        }
        if (x == -1) return;
        if (!deplacementValide(grille, x, y, dir)) return;

        int nx = x, ny = y;
        switch (dir) {
            case 'z': nx = x - 1; break;
            case 's': nx = x + 1; break;
            case 'q': ny = y - 1; break;
            case 'd': ny = y + 1; break;
        }
        if (grille[nx][ny] == CAISSE || grille[nx][ny] == CAISSE_OK) {
            deplacerCaisse(grille, x, y, dir);
        }
        grille[x][y] = LIBRE;
        grille[nx][ny] = JOUEUR;
    }

    static boolean partieTerminee(char[][] grille) {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE) return false;
            }
        }
        return true;
    }

    static void jouer() {
        char[][] grille = chargerNiveau(1);
        Scanner sc = new Scanner(System.in);
        int mouvements = 0;

        while (true) {
            afficherGrille(grille);
            if (partieTerminee(grille)) {
                System.out.println("Victoire !");
                System.out.println("Nombre de mouvements : " + mouvements);
                break;
            }
            System.out.print("Commande (z/s/q/d/x) : ");
            String cmd = sc.next();
            if (cmd.equals("x")) break;
            if (cmd.length() != 1) continue;
            char dir = cmd.charAt(0);

            if (dir == 'z' || dir == 's' || dir == 'q' || dir == 'd') {
                int jx = -1, jy = -1;
                for (int i = 0; i < grille.length; i++) {
                    for (int j = 0; j < grille[i].length; j++) {
                        if (grille[i][j] == JOUEUR) {
                            jx = i; jy = j;
                        }
                    }
                }
                if (jx != -1 && deplacementValide(grille, jx, jy, dir)) {
                    deplacerJoueur(grille, dir);
                    mouvements++;
                } else {
                    System.out.println("Déplacement invalide.");
                }
            } else {
                System.out.println("Commande invalide.");
            }
        }
        sc.close();
    }

    static void resoudreDFS(char[][] grille) {
        int jx = -1, jy = -1;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == JOUEUR) {
                    jx = i; jy = j;
                }
            }
        }

        int nbCaisses = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    nbCaisses++;
                }
            }
        }

        int[][] caisses = new int[nbCaisses][2];
        int k = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    caisses[k][0] = i;
                    caisses[k][1] = j;
                    k++;
                }
            }
        }

        State etatInitial = new State(jx, jy, caisses);

        Pile pile = new Pile(10000);
        EnsembleEtats visites = new EnsembleEtats(10000);
        pile.push(etatInitial);
        visites.ajouter(etatInitial);

        int etatsExplores = 0;

        while (!pile.isEmpty()) {
            State courant = pile.pop();
            etatsExplores++;

            if (estVictoire(courant, grille)) {
                System.out.println("DFS : solution trouvée !");
                System.out.println("Solution : " + courant.chemin);
                System.out.println("Nombre de mouvements : " + courant.chemin.length());
                System.out.println("États explorés : " + etatsExplores);
                return;
            }

            char[] directions = {'z', 's', 'q', 'd'};
            for (int i = 0; i < directions.length; i++) {
                State suivant = etatSuivant(courant, directions[i], grille);
                if (suivant != null && !visites.contient(suivant)) {
                    visites.ajouter(suivant);
                    pile.push(suivant);
                }
            }
        }

        System.out.println("DFS : pas de solution trouvée");
        System.out.println("États explorés : " + etatsExplores);
    }

    // ==================== PARTIE AXELLE — SOLVEUR ====================
    static class State {
        int joueurX, joueurY;
        int[][] caisses;
        String chemin;

        State(int jx, int jy, int[][] c) {
            this.joueurX = jx;
            this.joueurY = jy;
            this.caisses = c;
            this.chemin = "";
        }

        boolean equals(State autre) {
            if (autre == null) return false;
            if (this.joueurX != autre.joueurX || this.joueurY != autre.joueurY) return false;
            if (this.caisses.length != autre.caisses.length) return false;
            for (int i = 0; i < this.caisses.length; i++) {
                if (this.caisses[i][0] != autre.caisses[i][0]) return false;
                if (this.caisses[i][1] != autre.caisses[i][1]) return false;
            }
            return true;
        }

        State copie() {
            int[][] copieCaisses = new int[caisses.length][2];
            for (int i = 0; i < caisses.length; i++) {
                copieCaisses[i][0] = caisses[i][0];
                copieCaisses[i][1] = caisses[i][1];
            }
            State s = new State(joueurX, joueurY, copieCaisses);
            s.chemin = this.chemin;
            return s;
        }
    }

    static class File {
        private State[] tab;
        private int debut, fin, taille;

        File(int capacite) {
            tab = new State[capacite];
            debut = 0;
            fin = 0;
            taille = 0;
        }

        void enqueue(State s) {
            if (taille == tab.length) {
                System.out.println("File pleine !");
                return;
            }
            tab[fin] = s;
            fin = (fin + 1) % tab.length;
            taille++;
        }

        State dequeue() {
            if (taille == 0) return null;
            State s = tab[debut];
            debut = (debut + 1) % tab.length;
            taille--;
            return s;
        }

        boolean isEmpty() {
            return taille == 0;
        }

        int size() {
            return taille;
        }
    }

    // ----- Pile (Cheyma) -----
    static class Pile {
        private State[] tab;
        private int sommet;

        Pile(int capacite) {
            tab = new State[capacite];
            sommet = -1;
        }

        void push(State s) {
            if (sommet + 1 < tab.length) {
                sommet++;
                tab[sommet] = s;
            }
        }

        State pop() {
            if (sommet == -1) return null;
            State s = tab[sommet];
            sommet--;
            return s;
        }

        boolean isEmpty() {
            return sommet == -1;
        }

        int size() {
            return sommet + 1;
        }
    }

    static class EnsembleEtats {
        private State[] tab;
        private int taille;

        EnsembleEtats(int capacite) {
            tab = new State[capacite];
            taille = 0;
        }

        void ajouter(State s) {
            if (taille < tab.length) {
                tab[taille] = s;
                taille++;
            }
        }

        boolean contient(State s) {
            for (int i = 0; i < taille; i++) {
                if (tab[i].equals(s)) return true;
            }
            return false;
        }

        int size() {
            return taille;
        }
    }

    static class Resultat {
        boolean trouve;
        int etatsExplores;
        String chemin;

        Resultat(boolean t, int e, String c) {
            this.trouve = t;
            this.etatsExplores = e;
            this.chemin = c;
        }
    }

    static boolean estVictoire(State s, char[][] grille) {
        for (int i = 0; i < s.caisses.length; i++) {
            int cx = s.caisses[i][0];
            int cy = s.caisses[i][1];
            if (grille[cx][cy] != OBJECTIF && grille[cx][cy] != CAISSE_OK) {
                return false;
            }
        }
        return true;
    }

    static State etatSuivant(State s, char dir, char[][] modele) {
        char[][] grille = new char[modele.length][];
        for (int i = 0; i < modele.length; i++) {
            grille[i] = modele[i].clone();
        }

        grille[s.joueurX][s.joueurY] = LIBRE;

        for (int i = 0; i < s.caisses.length; i++) {
            int cx = s.caisses[i][0];
            int cy = s.caisses[i][1];
            if (grille[cx][cy] == OBJECTIF || grille[cx][cy] == CAISSE_OK) {
                grille[cx][cy] = CAISSE_OK;
            } else {
                grille[cx][cy] = CAISSE;
            }
        }

        grille[s.joueurX][s.joueurY] = JOUEUR;

        int dx = 0, dy = 0;
        switch (dir) {
            case 'z': dx = -1; break;
            case 's': dx = 1; break;
            case 'q': dy = -1; break;
            case 'd': dy = 1; break;
            default: return null;
        }

        int nx = s.joueurX + dx;
        int ny = s.joueurY + dy;

        if (nx < 0 || nx >= grille.length || ny < 0 || ny >= grille[0].length) return null;
        if (grille[nx][ny] == MUR) return null;

        State nouveau = s.copie();

        if (grille[nx][ny] == CAISSE || grille[nx][ny] == CAISSE_OK) {
            int dx2 = nx + dx;
            int dy2 = ny + dy;
            if (dx2 < 0 || dx2 >= grille.length || dy2 < 0 || dy2 >= grille[0].length) return null;
            if (grille[dx2][dy2] == MUR || grille[dx2][dy2] == CAISSE || grille[dx2][dy2] == CAISSE_OK) return null;

            for (int i = 0; i < nouveau.caisses.length; i++) {
                if (nouveau.caisses[i][0] == nx && nouveau.caisses[i][1] == ny) {
                    nouveau.caisses[i][0] = dx2;
                    nouveau.caisses[i][1] = dy2;
                    break;
                }
            }
        }

        nouveau.joueurX = nx;
        nouveau.joueurY = ny;
        nouveau.chemin = s.chemin + dir;

        return nouveau;
    }

    static void resoudreBFS(char[][] grille) {
        int jx = -1, jy = -1;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == JOUEUR) {
                    jx = i; jy = j;
                }
            }
        }

        int nbCaisses = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    nbCaisses++;
                }
            }
        }

        int[][] caisses = new int[nbCaisses][2];
        int k = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    caisses[k][0] = i;
                    caisses[k][1] = j;
                    k++;
                }
            }
        }

        State etatInitial = new State(jx, jy, caisses);

        File file = new File(10000);
        EnsembleEtats visites = new EnsembleEtats(10000);
        file.enqueue(etatInitial);
        visites.ajouter(etatInitial);

        int etatsExplores = 0;

        while (!file.isEmpty()) {
            State courant = file.dequeue();
            etatsExplores++;

            if (estVictoire(courant, grille)) {
                System.out.println("BFS : solution trouvée !");
                System.out.println("Solution : " + courant.chemin);
                System.out.println("Nombre de mouvements : " + courant.chemin.length());
                System.out.println("États explorés : " + etatsExplores);
                return;
            }

            char[] directions = {'z', 's', 'q', 'd'};
            for (int i = 0; i < directions.length; i++) {
                State suivant = etatSuivant(courant, directions[i], grille);
                if (suivant != null && !visites.contient(suivant)) {
                    visites.ajouter(suivant);
                    file.enqueue(suivant);
                }
            }
        }

        System.out.println("BFS : pas de solution trouvée");
        System.out.println("États explorés : " + etatsExplores);
    }

    static Resultat resoudreBFSResultat(char[][] grille) {
        int jx = -1, jy = -1;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == JOUEUR) { jx = i; jy = j; }
            }
        }
        int nbCaisses = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) nbCaisses++;
            }
        }
        int[][] caisses = new int[nbCaisses][2];
        int k = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    caisses[k][0] = i; caisses[k][1] = j; k++;
                }
            }
        }
        State etatInitial = new State(jx, jy, caisses);
        File file = new File(10000);
        EnsembleEtats visites = new EnsembleEtats(10000);
        file.enqueue(etatInitial);
        visites.ajouter(etatInitial);
        int etatsExplores = 0;

        while (!file.isEmpty()) {
            State courant = file.dequeue();
            etatsExplores++;
            if (estVictoire(courant, grille)) {
                return new Resultat(true, etatsExplores, courant.chemin);
            }
            char[] directions = {'z', 's', 'q', 'd'};
            for (int i = 0; i < directions.length; i++) {
                State suivant = etatSuivant(courant, directions[i], grille);
                if (suivant != null && !visites.contient(suivant)) {
                    visites.ajouter(suivant);
                    file.enqueue(suivant);
                }
            }
        }
        return new Resultat(false, etatsExplores, "");
    }

    static Resultat resoudreDFSResultat(char[][] grille) {
        int jx = -1, jy = -1;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == JOUEUR) { jx = i; jy = j; }
            }
        }
        int nbCaisses = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) nbCaisses++;
            }
        }
        int[][] caisses = new int[nbCaisses][2];
        int k = 0;
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[i].length; j++) {
                if (grille[i][j] == CAISSE || grille[i][j] == CAISSE_OK) {
                    caisses[k][0] = i; caisses[k][1] = j; k++;
                }
            }
        }
        State etatInitial = new State(jx, jy, caisses);
        Pile pile = new Pile(10000);
        EnsembleEtats visites = new EnsembleEtats(10000);
        pile.push(etatInitial);
        visites.ajouter(etatInitial);
        int etatsExplores = 0;

        while (!pile.isEmpty()) {
            State courant = pile.pop();
            etatsExplores++;
            if (estVictoire(courant, grille)) {
                return new Resultat(true, etatsExplores, courant.chemin);
            }
            char[] directions = {'z', 's', 'q', 'd'};
            for (int i = 0; i < directions.length; i++) {
                State suivant = etatSuivant(courant, directions[i], grille);
                if (suivant != null && !visites.contient(suivant)) {
                    visites.ajouter(suivant);
                    pile.push(suivant);
                }
            }
        }
        return new Resultat(false, etatsExplores, "");
    }

    static void comparerDFS_BFS(char[][] grille) {
        System.out.println("========== COMPARAISON ==========");

        long debutDFS = System.currentTimeMillis();
        Resultat resDFS = resoudreDFSResultat(grille);
        long finDFS = System.currentTimeMillis();

        long debutBFS = System.currentTimeMillis();
        Resultat resBFS = resoudreBFSResultat(grille);
        long finBFS = System.currentTimeMillis();

        System.out.println("DFS");
        System.out.println("Solution trouvée : " + (resDFS.trouve ? "oui" : "non"));
        if (resDFS.trouve) {
            System.out.println("Solution : " + resDFS.chemin);
            System.out.println("Nombre de mouvements : " + resDFS.chemin.length());
        }
        System.out.println("États explorés : " + resDFS.etatsExplores);
        System.out.println("Temps : " + (finDFS - debutDFS) + " ms");
        System.out.println();

        System.out.println("BFS");
        System.out.println("Solution trouvée : " + (resBFS.trouve ? "oui" : "non"));
        if (resBFS.trouve) {
            System.out.println("Solution : " + resBFS.chemin);
            System.out.println("Nombre de mouvements : " + resBFS.chemin.length());
        }
        System.out.println("États explorés : " + resBFS.etatsExplores);
        System.out.println("Temps : " + (finBFS - debutBFS) + " ms");
    }
}