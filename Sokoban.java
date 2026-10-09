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

    static boolean deplacementValide(char[][] grille, int x, int y, char dir) { return false; }
    static boolean peutPousser(char[][] grille, int x, int y, char dir) { return false; }
    static void deplacerCaisse(char[][] grille, int x, int y, char dir) { }
    static void deplacerJoueur(char[][] grille, char dir) { }
    static boolean partieTerminee(char[][] grille) { return false; }
    static void jouer() { }
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

    static void resoudreBFS(char[][] grille) {
        System.out.println("BFS : à implémenter");
    }

    static void comparerDFS_BFS(char[][] grille) {
        System.out.println("Comparaison DFS/BFS : à implémenter");
    }
}