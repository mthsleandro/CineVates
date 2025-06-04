package view;

import model.Arquivo;
import model.Entrada;

public class Main {

    public static void main(String[] args) {
        final int qtdOpcoes = 12;
        String[] vetMenu = new String[qtdOpcoes];
        vetMenu[0] = "Consultar filmes";
        vetMenu[1] = "Adicionar filme";
        vetMenu[2] = "Editar filme";
        vetMenu[3] = "Remover filme do catálogo";
        vetMenu[4] = "Comprar ingressos";
        vetMenu[5] = "Alterar ingresso";
        vetMenu[6] = "Invalidar ingresso";
        vetMenu[7] = "Consultar salas de cinema";
        vetMenu[8] = "Criar sala de cinema";
        vetMenu[9] = "Editar sala de cinema";
        vetMenu[10] = "Remover sala de cinema";
        vetMenu[11] = "Consultar assentos";

        Arquivo filmes = new Arquivo("filmes.txt");
        Arquivo salas = new Arquivo("salas.txt");
        Arquivo ingressos = new Arquivo("ingressos.txt");

        String vetStr = retornaVetorStr(vetMenu, true);
        String mensagem = "Bem vindo(a), selecione a opção desejada: " + vetStr;
        String input = "";
        int opcao;

        while (!input.equalsIgnoreCase("X")) {
            input = Entrada.leiaString(mensagem);

            while (!validaOpcao(vetMenu, input)) {
                input = Entrada.leiaString("Informe uma opção válida:");
            }
            opcao = Integer.parseInt(input);

            if (opcao == 1) {

            } else if (opcao == 2) {

            } else if (opcao == 3) {

            } else if (opcao == 4) {

            } else if (opcao == 5) {

            } else if (opcao == 6) {

            } else if (opcao == 7) {

            } else if (opcao == 8) {

            } else if (opcao == 9) {

            } else if (opcao == 10) {

            } else if (opcao == 11) {

            } else if (opcao == 12) {

            } else {
                mensagem = "Informe uma opção válida:";
            }
        }

    }

    private static boolean validaOpcao(String[] vetor, String opcao) {
        int pos = 0;
        try {
            pos = Integer.parseInt(opcao);
        } catch (NumberFormatException ex) {
            return false;
        }

        if (pos <= 0 || pos > vetor.length) {
            return false;
        }

        return true;
    }

    private boolean validaVetor(String[][] vetor) {
        boolean valido = false;

        return valido;
    }

    private static String retornaVetorStr(String[] vetor, boolean incluiPosicao) {
        String str = "";
        String aux;
        for (int i = 0; i < vetor.length; i++) {
            aux = "";
            if (incluiPosicao) {
                aux = String.valueOf(i + 1) + " - ";
            }
            str += "\n" + aux + vetor[i];
        }
        return str;
    }

    private static void imprimeVetor() {

    }
}
