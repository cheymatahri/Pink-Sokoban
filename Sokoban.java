public class Sokoban {

    // ==================== DONNÉES GLOBALES ====================
    static final char MUR      = '#';
    static final char LIBRE    = ' ';
    static final char JOUEUR   = '@';
    static final char CAISSE   = '$';
    static final char OBJECTIF = '.';
    static final char CAISSE_OK= '*';

    static final String[][] NIVEAUX = {
        { // Niveau 1 — poussée horizontale
            "##########",
            "#        #",
            "# @$ .   #",
            "#        #",
            "#        #",
            "#        #",
            "##########"
        }
    };

    // ==================== MAIN (temporaire, le temps que Cheyma teste) ====================
    public static void main(String[] args) {
        char[][] grille = chargerNiveau(1);
        afficherGrille(grille);
        // menu();  ← décommenter quand Axelle aura écrit menu()
    }

    // ==================== MÉTHODES (CHEYMA) ====================
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

    // ==================== MÉTHODES (AXELLE) — à remplir par Axelle ====================
    // static void menu() { ... }
    // static void resoudreBFS(char[][] grille) { ... }
    // static void comparerDFS_BFS(char[][] grille) { ... }
    // static class State { ... }
    // static class Pile { ... }
    // static class File { ... }
    // static class EnsembleEtats { ... }
}