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
        String[] vetFilmes = new String[contaLinhas(filmes)];
        for (int i = 0; i < vetFilmes.length; i++) {
            vetFilmes[i] = filmes.lerLinha();
        }

        final int POS_FLM_COD = 0; // ,
        final int POS_FLM_NOM = 1; // ,
        final int POS_FLM_HOR = 2; // ,
        final int POS_FLM_CLA = 3; // ,
        final int POS_FLM_VAL = 4;

        Arquivo salas = new Arquivo("salas.txt");
        String[] vetSalas = new String[contaLinhas(salas)];
        for (int i = 0; i < vetSalas.length; i++) {        
            vetSalas[i] = salas.lerLinha();
        }

        final int POS_SAL_COD = 0; // ,
        final int POS_SAL_FLM = 1; // #
        final int POS_SAL_ASS = 2; // -
        final int POS_SAL_VAG = 3;

        Arquivo ingressos = new Arquivo("ingressos.txt");
        String[] vetIngressos = new String[contaLinhas(ingressos)];
        for (int i = 0; i < vetIngressos.length; i++) {        
            vetIngressos[i] = ingressos.lerLinha();
        }

        Arquivo copia = new Arquivo("copia.txt");

        final int POS_ING_COD = 0; // ,
        final int POS_ING_FLM = 1; // ,
        final int POS_ING_SAL = 2; // ,
        final int POS_ING_ASS = 3; // ,
        final int POS_ING_HOR = 4;

        String vetStr = imprimeVetor(vetMenu, true);
        String mensagem = "Bem vindo(a), selecione a opção desejada: " + vetStr;
        String input = "";

        int opcao;
        while (!input.equalsIgnoreCase("X")) {
            input = Entrada.leiaString(mensagem);

            if (input.equalsIgnoreCase("X")) { 
                break; 
            }

            while (!validaOpcao(vetMenu, input)) {
                input = Entrada.leiaString("Informe uma opção válida");
            }
            opcao = Integer.parseInt(input);

            if (opcao == 1) { // Consultar filmes
                filmes.abrirLeitura();
                String[] vetAux;
                if (Entrada.leiaBoolean("Deseja ver todos os filmes disponíveis?")) {
                    vetAux = vetFilmes;
                } else {
                    Double hora = Entrada.leiaDouble("Informe o horário desejado");
                    vetAux = new String[vetFilmes.length];
                    Double horaAux;
                    String parte;

                    for (int i = 0; i < vetFilmes.length; i++) {
                        parte = piece(vetFilmes[i], ",", POS_FLM_HOR);
                        try {
                            horaAux = Double.valueOf(parte);
                            if (horaAux >= hora) {
                                vetAux[i] = vetFilmes[i];
                            }
                        } catch (NumberFormatException ex) {

                        }
                    }
                }
                System.out.println("Filmes disponíveis no catálogo:");
                System.out.println(imprimeVetor(vetAux, true));

            } else if (opcao == 2) { // Adicionar filme
                boolean verif = true;

                String[] vetor = new String[10];
                String nome;
                int horario;
                int classificacao;
                double valor;
                int x = 0;

                // verificar ultimo número do filme: 1, nome, ... 2, nome...
                 filmes.fecharArquivo();
                filmes.abrirLeitura();
                String ultimaLinha = null;
                String linha = filmes.lerLinha();
                while (linha != null) { // encontra a ultima linha do txt
                    ultimaLinha = linha;
                    linha = filmes.lerLinha();
                }
                filmes.fecharArquivo();
                if (ultimaLinha != null) {
                    try {  // verifica qual é o número do filme da ultima linha quando achar a primeira vírgula
                        String cod = piece(ultimaLinha, ",", POS_FLM_COD);
                        x = Integer.parseInt(cod.trim()) + 1;
                    } catch (NumberFormatException ex) {
                        
                    }
                }

                while (verif == true) {
                    nome = Entrada.leiaString("Digite o nome do filme");
                    horario = Entrada.leiaInt("Digie o horário que irá começar o filme");
                    classificacao = Entrada.leiaInt("Digite a classificação mínima para assistir filme");
                    valor = Entrada.leiaDouble("Digite o valor que será cobrado por ingresso");

                    vetor[x] = x + "," + nome + "," + horario + "," + classificacao + "," + valor;
                    x++;

                    if (x > vetor.length - 1) {
                        break;
                    }
                    verif = Entrada.leiaBoolean("Deseja cadastrar mais algum filme?");
                }
                cadastraFilme(filmes, vetor);

            } else if (opcao == 3) { // Editar filme
                filmes.abrirEscrita();
                String linha = filmes.lerLinha();

                copia.abrirEscrita(false);
                int numerofilme = Entrada.leiaInt("Digite o número do filme que quer editar");
                editarFilme(filmes, numerofilme, linha, copia);

            } else if (opcao == 4) { // Remover filme do catálogo
                filmes.abrirLeitura();
                copia.abrirEscrita(false);

                int codRemover = Entrada.leiaInt("Digite o código do filme que deseja remover:");
                String linha = filmes.lerLinha();

                while (linha != null) {
                    try {
                        int cod = Integer.parseInt(piece(linha,",",POS_FLM_COD));
                        if (cod != codRemover) {
                            copia.escreverLinha(linha);
                        }
                    } catch (NumberFormatException ex) {
                        copia.escreverLinha(linha);
                    }
                    linha = filmes.lerLinha();
                }
                filmes.fecharArquivo();
                copia.fecharArquivo();

                if (filmes.deletarArquivo()) {
                    copia.renomearPara(filmes.getNome());
                    System.out.println("Filme removido com sucesso.");
                } else {
                    System.out.println("Erro ao remover filme.");
                }
            } else if (opcao == 5) { // Comprar ingresso
                String codFilme = Entrada.leiaString("Digite o código do filme");
                String codSala = Entrada.leiaString("Digite o código da sala");
                String assento = Entrada.leiaString("Digite o número do assento");
                String horario = Entrada.leiaString("Digite o horário do filme");

                // Gerar código do ingresso automaticamente
                ingressos.abrirLeitura();
                String linha;
                String ultimALinha = null;

                while ((linha = ingressos.lerLinha()) != null) {
                    ultimALinha = linha;
                }
                ingressos.fecharArquivo();

                int cod = 1;
                if (ultimALinha != null) {
                    try {
                        cod = Integer.parseInt(piece(ultimALinha,",",POS_FLM_COD)) + 1;
                    } catch (NumberFormatException ex) {
                        
                    }
                }

                ingressos.abrirEscrita(true);
                ingressos.escreverLinha(cod + "," + codFilme + "," + codSala + "," + assento + "," + horario);
                ingressos.fecharArquivo();

                System.out.println("Ingresso comprado com sucesso!");

            } else if (opcao == 6) { // Cancelar ingresso
                ingressos.abrirLeitura();
                copia.abrirEscrita(false);

                int codCancelar = Entrada.leiaInt("Digite o código do ingresso a ser cancelado:");
                String linha = ingressos.lerLinha();

                while (linha != null) {
                    try {
                        int cod = Integer.parseInt(piece(linha,",",POS_ING_COD));
                        if (cod != codCancelar) {
                            copia.escreverLinha(linha);
                        }
                    } catch (NumberFormatException ex) {
                        copia.escreverLinha(linha);
                    }
                    linha = ingressos.lerLinha();
                }
                ingressos.fecharArquivo();
                copia.fecharArquivo();

                if (ingressos.deletarArquivo()) {
                    copia.renomearPara(ingressos.getNome());
                    System.out.println("Ingresso cancelado com sucesso.");
                } else {
                    System.out.println("Erro ao cancelar ingresso.");
                }
            } else if (opcao == 7) { // Consultar salas de cinema
                salas.abrirLeitura();
                String linha = salas.lerLinha();
                String resultado = "=== SALAS DE CINEMA ===\n";

                while (linha != null) {
                    resultado += linha + "\n";
                    linha = salas.lerLinha();
                }
                salas.fecharArquivo();
                System.out.println(resultado);

            } else if (opcao == 8) { // Criar sala de cinema
                int codSala = Entrada.leiaInt("Digite o código da nova sala:");
                String assentos = Entrada.leiaString("Digite os assentos no formato 1-V,2-V,3-V:");
                salas.abrirEscrita(true);
                salas.escreverLinha(codSala + "#" + assentos);
                salas.fecharArquivo();
                System.out.println("Sala cadastrada com sucesso.");

            } else if (opcao == 9) { // Editar sala de cinema 
                salas.abrirLeitura();
                copia.abrirEscrita(false);

                int codEditar = Entrada.leiaInt("Digite o código da sala que deseja editar:");
                String linha = salas.lerLinha();
                boolean editado = false;

                while (linha != null) {
                    try {
                        String[] partes = linha.split("#");
                        int cod = Integer.parseInt(partes[0].trim());
                        if (cod == codEditar) {
                            String novosAssentos = Entrada.leiaString("Digite os novos assentos no formato 1-V,2-V,3-V:");
                            copia.escreverLinha(cod + "#" + novosAssentos);
                            editado = true;
                        } else {
                            copia.escreverLinha(linha);
                        }
                    } catch (Exception e) {
                        copia.escreverLinha(linha); // mantém se não conseguir ler
                    }
                    linha = salas.lerLinha();
                }

                salas.fecharArquivo();
                copia.fecharArquivo();

                if (salas.deletarArquivo()) {
                    copia.renomearPara(salas.getNome());
                    if (editado) {
                        System.out.println("Sala editada com sucesso.");
                    } else {
                        System.out.println("Sala não encontrada.");
                    }
                } else {
                    System.out.println("Erro ao editar sala.");
                }
            } else if (opcao == 10) { // Remover sala de cinema
                salas.abrirLeitura();
                copia.abrirEscrita(false);

                int codRemover = Entrada.leiaInt("Digite o código da sala que deseja remover:");
                String linha = salas.lerLinha();
                boolean removido = false;

                while (linha != null) {
                    try {
                        String info = piece(linha,"#",1);
                        int cod = Integer.parseInt(piece(info,",",POS_SAL_COD));

                        if (cod != codRemover) {
                            copia.escreverLinha(linha);
                        } else {
                            removido = true;
                        }
                    } catch (Exception ex) {
                        copia.escreverLinha(linha);
                    }
                    linha = salas.lerLinha();
                }

                salas.fecharArquivo();
                copia.fecharArquivo();

                if (salas.deletarArquivo()) {
                    copia.renomearPara(salas.getNome());
                    if (removido) {
                        System.out.println("Sala removida com sucesso.");
                    } else {
                        System.out.println("Sala não encontrada.");
                    }
                } else {
                    System.out.println("Erro ao remover sala.");
                }

            } else if (opcao == 11) { // Consultar assentos
                int codSalaConsulta = Entrada.leiaInt("Digite o código da sala que deseja consultar:");
                salas.abrirLeitura();
                String linha = salas.lerLinha();
                boolean encontrou = false;

                while (linha != null) {
                    String info = piece(linha,"#",1);
                    int cod = Integer.parseInt(piece(info,",",POS_SAL_COD));

                    try {
                        if (cod == codSalaConsulta) {
                            String assentos = piece(linha,"#",2);
                            String[] lista = assentos.split(",");
                            
                            System.out.println("Assentos da sala " + cod + ":");
                            for (String a : lista) {
                                String codAssento = piece(a,"-",1);
                                String sit = piece(a,"-",2);
                                if (sit.equalsIgnoreCase("O")) {
                                    sit = "Ocupado";
                                } else {
                                    sit = "Vago";
                                }

                                System.out.println("- Assento " + codAssento + " - " + sit);
                            }
                            encontrou = true;
                            break;
                        }
                    } catch (Exception ex) {
                        
                    }
                    linha = salas.lerLinha();
                }
                salas.fecharArquivo();

                if (!encontrou) {
                    System.out.println("Sala não encontrada.");
                }
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
            if (vetor[i] == null) {
                continue;
            }
            
            aux = "";
            if (incluiPosicao) {
                aux = String.valueOf(i + 1) + " - ";
            }
            str += "\n" + aux + vetor[i];
        }
        return str;
    }

    private static void cadastraFilme(Arquivo filmes, String[] vetor) {
        // formato do arquivo: 1,NOME,HORARIO,CLASSIFICACAO,VALOR,   
        filmes.abrirEscrita(true);
        for (int x = 0; x < vetor.length; x++) {
            if (vetor[x] != null) {
                filmes.escreverLinha(vetor[x]);
            }
        }
        filmes.fecharArquivo();
    }

    private static void editarFilme(Arquivo filmes, int numerofilme, String linha, Arquivo copia) {
        while (linha != null) {
            try {
                int numerolinha = Integer.parseInt(piece(linha, ",", 0));

                if (numerolinha == numerofilme) {
                    String nome = Entrada.leiaString("Digite o nome do filme");
                    int horario = Entrada.leiaInt("Digie o horário que irá começar o filme");
                    int classificacao = Entrada.leiaInt("Digite a classificação mínima para assistir filme");
                    double valor = Entrada.leiaDouble("Digite o valor que será cobrado por ingresso");

                    String linhanova = numerolinha + ", " + nome + ", " + horario + ", " + classificacao + ", " + valor;
                    copia.escreverLinha(linhanova);

                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException e) {
                copia.escreverLinha(linha);
            }
            copia.lerLinha();
        }
        filmes.fecharArquivo();
        copia.fecharArquivo();

        if (filmes.deletarArquivo()) {
            copia.renomearPara(filmes.getNome());
        }
    }

    private void cadastraSala(String codFilme, String assentos) {
        /* formato do arquivo:  SALA.FILME#ASSENTO-VAGO
        Exemplo: 1,1#1-V,2-F*/
    }

    private void cadastraIngresso(String codFilme, String codSala, String assento, String horario) {
        // formato do arquivo: CODIGO,FILME,SALA,ASSENTO,HORARIO
    }

    private static int contaLinhas(Arquivo arquivo) {
        arquivo.abrirLeitura();

        int cont = 0;
        String linha = arquivo.lerLinha();
        while (linha != null) {
            cont++;
            linha = arquivo.lerLinha();
        }
        arquivo.fecharArquivo();
        arquivo.abrirLeitura();
        return cont;
    }

    private static String piece(String texto, String delim, int parte) {
        String res = "";
        String partes[] = texto.split(delim);
        if (parte <= partes.length) {
            res = partes[parte];
        }
        return res;
    }

}
