package view;

import model.Arquivo;
import model.Entrada;

public class Main {

    public static void main(String[] args) {
        final int qtdOpcoes = 11;
        String[] vetMenu = new String[qtdOpcoes];
        vetMenu[0] = "Consultar filmes";
        vetMenu[1] = "Adicionar filme";
        vetMenu[2] = "Editar filme";
        vetMenu[3] = "Remover filme do catálogo";
        vetMenu[4] = "Comprar ingressos";
        vetMenu[5] = "Cancelar ingresso";
        vetMenu[6] = "Consultar salas de cinema";
        vetMenu[7] = "Criar sala de cinema";
        vetMenu[8] = "Editar sala de cinema";
        vetMenu[9] = "Remover sala de cinema";
        vetMenu[10] = "Consultar assentos";

        Arquivo filmes = new Arquivo("filmes.txt");
        // 1,NOME,HORARIO,CLASSIFICACAO,VALOR,
        Arquivo salas = new Arquivo("salas.txt");
        /*SALA.FILME#ASSENTO-VAGO
          1,1#1-V,2-F*/
        Arquivo ingressos = new Arquivo("ingressos.txt");
        // CODIGO,FILME,SALA,ASSENTO,HORARIO

        String vetStr = retornaVetorStr(vetMenu, true);
        String mensagem = "Bem vindo(a), selecione a opção desejada: " + vetStr;
        String input = "";
        int opcao;

        while (!input.equalsIgnoreCase("X")) {
            input = Entrada.leiaString(mensagem);

            if (input.equalsIgnoreCase("X")) {
                break;
            }

            while (!validaOpcao(vetMenu, input)) {
                input = Entrada.leiaString("Informe uma opção válida:");
            }
            opcao = Integer.parseInt(input);

            if (opcao == 1) { // Consultar filmes
                filmes.abrirLeitura();
            } else if (opcao == 2) { // Adicionar filme
                filmes.abrirEscrita();
            } else if (opcao == 3) { // Editar filme
                filmes.abrirEscrita();
            } else if (opcao == 4) { // Remover filme do catálogo
                filmes.abrirEscrita();
            } else if (opcao == 5) { // Comprar ingresso

            } else if (opcao == 6) { // Cancelar ingresso

            } else if (opcao == 7) { // Consultar salas de cinema

            } else if (opcao == 8) { // Criar sala de cinema

            } else if (opcao == 9) { // Editar sala de cinema 

            } else if (opcao == 10) { // Remover sala de cinema

            } else if (opcao == 11) { // Consultar assentos

            } else {
                mensagem = "Informe uma opção válida:";
                continue;
            }
            mensagem = "Selecione uma opção:";
        }
        filmes.fecharArquivo();
        salas.fecharArquivo();
        ingressos.fecharArquivo();
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

    private boolean achaNoVetor(String[] vetor) {
        boolean achou = false;

        return achou;
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

    private void cadastraFilme() {

    }

    private void cadastraSala() {

    }

    private void cadastraIngresso() {

    }

    private static void imprimeVetor() {

    }
}
