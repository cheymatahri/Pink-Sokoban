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

        while (choix != 4) {
            System.out.println("======== SOKOBAN ========");
            System.out.println("1. Niveau 1");
            System.out.println("4. Quitter");
            System.out.print("Votre choix : ");

            if (sc.hasNextInt()) {
                choix = sc.nextInt();
            } else {
                sc.next();
                choix = 0;
            }

            if (choix == 1) {
                char[][] grille = chargerNiveau(1);
                afficherGrille(grille);
            } else if (choix == 4) {
                System.out.println("Au revoir !");
            } else {
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

    static void resoudreDFS(char[][] grille) { }


    // ==================== PARTIE AXELLE — SOLVEUR ====================
    static class State {
        int joueurX, joueurY;
        int[][] caisses;

        State(int jx, int jy, int[][] c) {
            this.joueurX = jx;
            this.joueurY = jy;
            this.caisses = c;
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
            return new State(joueurX, joueurY, copieCaisses);
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
        System.out.println("BFS : état initial créé");
        System.out.println("Joueur en (" + jx + ", " + jy + ")");
        System.out.println("Nombre de caisses : " + nbCaisses);
    }

    static void comparerDFS_BFS(char[][] grille) {
        System.out.println("Comparaison DFS/BFS : à implémenter");
    }
}