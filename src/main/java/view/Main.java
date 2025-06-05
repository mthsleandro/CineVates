package view;

import model.Arquivo;
import model.Entrada;

public class Main {

    public static void main(String[] args) {
        String[] vetMenu = new String[11];
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
        Arquivo salas = new Arquivo("salas.txt");
        Arquivo ingressos = new Arquivo("ingressos.txt");

        String vetStr = imprimeVetor(vetMenu, true);
        String mensagem = "Bem vindo(a), selecione a opção desejada: " + vetStr;
        String input = "";
        int opcao;

        while (!input.equalsIgnoreCase("X")) {
            input = Entrada.leiaString(mensagem);

            if (input.equalsIgnoreCase("X")) { // se igual a "x" ou "X"
                break; // sai do loop
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
        } catch (NumberFormatException ex) { // se não for um número, resultará erro
            return false;
        }

        if (pos <= 0 || pos > vetor.length) { // se o número for menor que 0 ou maior que as opções do vetor do menu
            return false;
        }

        return true;
    }

    private boolean achaNoVetor(String[] vetor, String texto, int pos) {
        boolean achou = false;

        return achou;
    }

    private static String imprimeVetor(String[] vetor, boolean incluiPosicao) {
        // função para imprimir o vetor. Se passar incluiPosicao como true, imprime a posição no vetor junto
        // Ex: 1 - Consultar filmes
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

    // nas funções abaixo adicionar o novo registro dentro do vetor
    private void cadastraFilme(String vetor[], String nome, String horario, String classificacao, Double valor) {
        // formato do arquivo: 1,NOME,HORARIO,CLASSIFICACAO,VALOR,    
    }

    private void cadastraSala(String codFilme, String assentos) {
        /* formato do arquivo:  SALA.FILME#ASSENTO-VAGO
        Exemplo: 1,1#1-V,2-F*/
    }

    private void cadastraIngresso(String codFilme, String codSala, String assento, String horario) {
        // formato do arquivo: CODIGO,FILME,SALA,ASSENTO,HORARIO
    }

}
