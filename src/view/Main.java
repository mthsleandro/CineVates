package view;

import java.text.NumberFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
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

        final int POS_COD = 0;
        final int POS_HOR = 2;

        Arquivo filmes = new Arquivo("filmes.txt");
        Arquivo salas = new Arquivo("salas.txt");
        Arquivo ingressos = new Arquivo("ingressos.txt");
        Arquivo copia = new Arquivo("copia.txt");

        String vetStr = imprimeVetor(vetMenu, true);
        String mensagem = "Bem vindo(a), selecione a opção desejada: " + vetStr;
        String input = "";
        int opcao;

        String msgPadrao = "Selecione uma opção: " + vetStr;
        while (!input.equalsIgnoreCase("X")) {
            input = Entrada.leiaString(mensagem);
            if (input.equalsIgnoreCase("X")) {
                break;
            }

            while (!validaOpcao(vetMenu, input)) {
                input = Entrada.leiaString("Informe uma opção válida");
            }

            filmes = new Arquivo("filmes.txt");
            salas = new Arquivo("salas.txt");
            ingressos = new Arquivo("ingressos.txt");
            copia = new Arquivo("copia.txt");

            opcao = Integer.parseInt(input);
            if (opcao == 1) { // Consultar filmes
                String[] vetAux;
                String[] vetFilmes = new String[contaLinhas(filmes)];
                atualizaVetor(vetFilmes, filmes);

                boolean verif = true;
                if (Entrada.leiaBoolean("Deseja ver todos os filmes disponíveis?")) {
                    // imprime todos os filmes
                    vetAux = vetFilmes;
                } else {
                    // imprime os filmes do horário selecionado
                    Double hora = Entrada.leiaDouble("Informe o horário desejado");
                    vetAux = new String[vetFilmes.length];
                    Double horaAux;
                    String parte;

                    for (int i = 0; i < vetFilmes.length; i++) {
                        parte = piece(vetFilmes[i], ",", POS_HOR);
                        try {
                            horaAux = Double.valueOf(parte);
                            if (horaAux == hora) {
                                vetAux[i] = vetFilmes[i];
                            }
                        } catch (NumberFormatException ex) {
                            System.err.println("Ocorreu um erro ao buscar o horário do filme.");
                            verif = false;
                        }
                    }

                    if (vetAux.length <= 0) {
                        System.out.println("Não foi encontrado nenhum filme neste horário.");
                        mensagem = msgPadrao;
                        continue;
                    }
                }

                if (!verif) {
                    mensagem = msgPadrao;
                    continue;
                }
                imprimirFilmes(vetAux);

            } else if (opcao == 2) { // Adicionar filme
                String[] vetor = new String[10];
                String nome;
                int horario;
                int sala;
                int classificacao;
                double valor;
                int x = 0;

                filmes.fecharArquivo();
                filmes.abrirLeitura();
                String ultimaLinha = null;

                String linha = filmes.lerLinha();
                while (linha != null) {
                    ultimaLinha = linha;
                    linha = filmes.lerLinha();
                }
                filmes.fecharArquivo();

                boolean verif = true;
                if (ultimaLinha != null) {
                    try {
                        String cod = piece(ultimaLinha, ",", POS_COD);
                        x = Integer.parseInt(cod.trim()) + 1;
                    } catch (NumberFormatException ex) {
                        System.err.println("Ocorreu um erro ao definir o código do filme.");
                        verif = false;
                    }
                }

                if (!verif) {
                    mensagem = msgPadrao;
                    continue;
                }

                while (verif == true) {
                    nome = Entrada.leiaString("Digite o nome do filme");
                    horario = Entrada.leiaInt("Digie o horário que irá começar o filme");

                    String[] vetSalas = new String[contaLinhas(salas)];
                    atualizaVetor(vetSalas, salas);

                    String msg = "Digite a sala que irá apresentar o filme";
                    while (true) {
                        sala = Entrada.leiaInt(msg);
                        if (achaNoVetor(vetSalas, sala, "#")) {
                            break;
                        }
                        msg = "Informe uma sala válida.";
                    }

                    classificacao = Entrada.leiaInt("Digite a classificação mínima para assistir filme");
                    valor = Entrada.leiaDouble("Digite o valor que será cobrado por ingresso");

                    vetor[x] = x + "," + nome + "," + horario + "," + sala + "," + classificacao + "," + valor;
                    x++;

                    if (x > vetor.length - 1) {
                        break;
                    }
                    verif = Entrada.leiaBoolean("Deseja cadastrar mais algum filme?");
                }

                filmes.abrirEscrita(true);
                for (String filme : vetor) {
                    if (filme != null) {
                        filmes.escreverLinha(filme);
                    }
                }
                filmes.fecharArquivo();

            } else if (opcao == 3) { // Editar filme
                String[] vetFilmes = new String[contaLinhas(filmes)];
                atualizaVetor(vetFilmes, filmes);

                int numeroFilme = Entrada.leiaInt("Digite o número do filme que quer editar");
                if (achaNoVetor(vetFilmes, numeroFilme, ",")) {
                    editarFilme(filmes, numeroFilme, copia);
                } else {
                    System.err.println("Filme não encontrado.");
                }

            } else if (opcao == 4) { // Remover filme do catálogo
                String[] vetFilmes = new String[contaLinhas(filmes)];
                atualizaVetor(vetFilmes, filmes);

                int numeroFilme = Entrada.leiaInt("Digite o número do filme que quer excluir");
                if (achaNoVetor(vetFilmes, numeroFilme, ",")) {
                    // valida se existe ingressos com esse filme e pergunta se deseja cancelar
                    String[] vetIngressos = new String[contaLinhas(ingressos)];
                    atualizaVetor(vetIngressos, ingressos);
                    boolean temIngresso = false;

                    for (String ing : vetIngressos) {
                        if (piece(ing, ",", 1).equals(String.valueOf(numeroFilme))) {
                            temIngresso = true;
                            break;
                        }
                    }

                    if (temIngresso) {
                        boolean continuar = Entrada.leiaBoolean("Há ingressos associados a este filme. Deseja cancelá-los e continuar?");
                        if (!continuar) {
                            // mantém ingressos, mesmo que o filme seja excluído
                            mensagem = msgPadrao;
                            continue;
                        }

                        // cancela ingressos
                        ingressos.abrirEscrita(false);
                        for (String ing : vetIngressos) {
                            if (!piece(ing, ",", 1).equals(String.valueOf(numeroFilme))) {
                                ingressos.escreverLinha(ing);
                            }
                        }
                        ingressos.fecharArquivo();
                    }

                    excluirFilme(filmes, numeroFilme, copia);
                } else {
                    System.err.println("Filme não encontrado.");
                }
            } else if (opcao == 5) { // Comprar ingresso
                String codFilme = Entrada.leiaString("Digite o código do filme");
                String codSala = Entrada.leiaString("Digite o código da sala");
                String assento = Entrada.leiaString("Digite o número do assento");
                String horario = Entrada.leiaString("Digite o horário do filme");

                boolean filmeValido = false;
                boolean horarioValido = false;

                filmes.abrirLeitura();
                String linha = filmes.lerLinha();
                while (linha != null) {
                    String cod = piece(linha, ",", 0);
                    String hora = piece(linha, ",", 2);

                    if (cod.equals(codFilme)) {
                        filmeValido = true;
                        if (hora.equals(horario)) {
                            horarioValido = true;
                        }
                        break;
                    }
                    linha = filmes.lerLinha();
                }
                filmes.fecharArquivo();

                if (!filmeValido) {
                    System.err.println("Filme não encontrado.");
                    continue;
                }
                if (!horarioValido) {
                    System.err.println("Horário não corresponde ao filme.");
                    continue;
                }

                // Valida sala e assento, e armazena todas as linhas das salas em array
                boolean salaValida = false;
                boolean assentoDisponivel = false;
                String[] linhasSalas = new String[100]; // limite de 100 salas
                int totalSalas = 0;

                salas.abrirLeitura();
                String linhaSala;
                while ((linhaSala = salas.lerLinha()) != null && totalSalas < linhasSalas.length) {
                    String cod = piece(linhaSala, "#", 0);
                    String assentosStr = piece(linhaSala, "#", 1);

                    if (cod.equals(codSala)) {
                        salaValida = true;

                        String[] assentos = assentosStr.split(",");
                        for (int i = 0; i < assentos.length; i++) {
                            String codAssento = piece(assentos[i], "-", 0);
                            String situacao = piece(assentos[i], "-", 1);
                            if (codAssento.equals(assento)) {
                                if (situacao.equals("V")) {
                                    assentos[i] = codAssento + "-O"; // marca como ocupado
                                    assentoDisponivel = true;
                                }
                                break;
                            }
                        }

                        // Reconstrói a linha da sala com assento atualizado
                        String novaLinha = cod + "#" + assentos[0];
                        for (int i = 1; i < assentos.length; i++) {
                            novaLinha += "," + assentos[i];
                        }
                        linhasSalas[totalSalas] = novaLinha;

                    } else {
                        linhasSalas[totalSalas] = linhaSala;
                    }

                    totalSalas++;
                }
                salas.fecharArquivo();

                if (!salaValida) {
                    System.out.println("Sala não encontrada.");
                    continue;
                }
                if (!assentoDisponivel) {
                    System.out.println("Assento inválido ou já está ocupado.");
                    continue;
                }

                // Gera novo código de ingresso
                ingressos.abrirLeitura();
                String ultimaLinha = null;
                String linhaIngresso;
                while ((linhaIngresso = ingressos.lerLinha()) != null) {
                    ultimaLinha = linhaIngresso;
                }
                ingressos.fecharArquivo();

                int cod = 1;
                if (ultimaLinha != null) {
                    try {
                        cod = Integer.parseInt(piece(ultimaLinha, ",", POS_COD)) + 1;
                    } catch (NumberFormatException ex) {
                        System.err.println("Erro ao buscar o código do ingresso.");
                        mensagem = msgPadrao;
                        continue;
                    }
                }

                // Grava o ingresso
                ingressos.abrirEscrita(true); // append
                ingressos.escreverLinha(cod + "," + codFilme + "," + codSala + "," + assento + "," + horario);
                ingressos.fecharArquivo();

                // Sobrescreve o arquivo de salas com assento atualizado
                salas.abrirEscrita(false); // sobrescreve
                for (int i = 0; i < totalSalas; i++) {
                    if (linhasSalas[i] != null) {
                        salas.escreverLinha(linhasSalas[i]);
                    }
                }
                salas.fecharArquivo();

                System.out.println("Ingresso comprado com sucesso! Código do ingresso: " + cod);
            } else if (opcao == 6) { // Cancelar ingresso
                String[] vetIngressos = new String[contaLinhas(ingressos)];
                atualizaVetor(vetIngressos, ingressos);

                int numeroIng = Entrada.leiaInt("Digite o número do ingresso que deseja cancelar");
                if (achaNoVetor(vetIngressos, numeroIng, ",")) {
                    cancelarIngresso(ingressos, numeroIng, copia);
                } else {
                    System.err.println("Ingresso não encontrado.");
                }
            } else if (opcao == 7) { // Consultar salas de cinema
                String[] vetSalas = new String[contaLinhas(salas)];
                atualizaVetor(vetSalas, salas);
                imprimirSalas(vetSalas);

            } else if (opcao == 8) { // Criar sala de cinema
                int codSala = Entrada.leiaInt("Digite o código da nova sala:");
                String assentos = Entrada.leiaString("Digite os assentos no formato 1-V,2-V,3-V:");
                salas.abrirEscrita(true);
                salas.escreverLinha(codSala + "#" + assentos);
                salas.fecharArquivo();
                System.out.println("Sala cadastrada com sucesso.");

            } else if (opcao == 9) { // Editar sala de cinema 
                String[] vetSalas = new String[contaLinhas(salas)];
                atualizaVetor(vetSalas, salas);

                int numeroSala = Entrada.leiaInt("Digite o número da sala que deseja editar");
                if (achaNoVetor(vetSalas, numeroSala, "#")) {
                    editarSala(salas, numeroSala, copia);
                } else {
                    System.err.println("Sala não encontrada.");
                }

            } else if (opcao == 10) { // Remover sala de cinema
                String[] vetSalas = new String[contaLinhas(salas)];
                atualizaVetor(vetSalas, salas);

                int numeroSala = Entrada.leiaInt("Digite o número da sala que quer excluir");
                if (achaNoVetor(vetSalas, numeroSala, "#")) {
                    String[] vetIngressos = new String[contaLinhas(ingressos)];
                    atualizaVetor(vetIngressos, ingressos);
                    boolean temIngresso = false;

                    for (String ing : vetIngressos) {
                        if (piece(ing, ",", 2).equals(String.valueOf(numeroSala))) {
                            temIngresso = true;
                            break;
                        }
                    }

                    if (temIngresso) {
                        boolean continuar = Entrada.leiaBoolean("Há ingressos associados a esta sala. Deseja cancelá-los e continuar?");
                        if (!continuar) {
                            mensagem = msgPadrao;
                            continue;
                        }

                        ingressos.abrirEscrita(false);
                        for (String ing : vetIngressos) {
                            if (!piece(ing, ",", 2).equals(String.valueOf(numeroSala))) {
                                ingressos.escreverLinha(ing);
                            }
                        }
                        ingressos.fecharArquivo();
                    }

                    excluirSala(salas, numeroSala, copia);
                } else {
                    System.err.println("Sala não encontrada.");
                }

            } else if (opcao == 11) { // Consultar assentos
                int codSalaConsulta = Entrada.leiaInt("Digite o código da sala que deseja consultar:");
                String[] vetSalas = new String[contaLinhas(salas)];
                atualizaVetor(vetSalas, salas);
                String[] vetAux = new String[1];
                String linha;
                int aux;

                boolean verif = true;
                for (int i = 0; i < vetSalas.length; i++) {
                    linha = vetSalas[i];
                    if (linha == null || linha.trim().equals("")) {
                        continue;
                    }

                    try {
                        aux = Integer.parseInt(piece(linha, "#", 0));
                        if (codSalaConsulta == aux) {
                            vetAux[0] = vetSalas[i];
                            break;
                        }
                    } catch (NumberFormatException ex) {
                        verif = false;
                    }
                }

                if (verif) {
                    imprimirSalas(vetAux);
                } else {
                    System.out.println("Sala não encontrada.");
                }

            } else {
                mensagem = "Informe uma opção válida:";
                continue;
            }

            mensagem = msgPadrao;
        }

        filmes.fecharArquivo();
        salas.fecharArquivo();
        ingressos.fecharArquivo();
    }

    private static boolean validaOpcao(String[] menu, String entrada) {
        try {
            int opc = Integer.parseInt(entrada);
            return opc >= 1 && opc <= menu.length;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void editarFilme(Arquivo arq, int codEditar, Arquivo copia) {
        arq.fecharArquivo();
        arq.abrirLeitura();

        copia.fecharArquivo();
        copia.abrirEscrita();

        boolean editou = false;
        String linha = arq.lerLinha();
        while (linha != null) {
            try {
                int cod = Integer.parseInt(piece(linha, ",", 0));
                if (cod == codEditar) {
                    String nome = Entrada.leiaString("Novo nome do filme:");
                    int horario = Entrada.leiaInt("Novo horário:");
                    String sala = Entrada.leiaString("Nova sala:");
                    int classificacao = Entrada.leiaInt("Nova classificação:");
                    double valor = Entrada.leiaDouble("Novo valor:");
                    copia.escreverLinha(cod + "," + nome + "," + horario + "," + sala + "," + classificacao + "," + valor);
                    editou = true;
                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException ex) {
                editou = false;
                break;
            }
            linha = arq.lerLinha();
        }

        arq.fecharArquivo();
        copia.fecharArquivo();
        if (editou) {
            if (arq.deletarArquivo()) {
                copia.renomearPara(arq.getNome());
                System.out.println("Filme editado com sucesso.");
            }
        } else {
            System.out.println("Erro ao editar filme.");
        }
    }

    private static void excluirFilme(Arquivo arq, int codExcluir, Arquivo copia) {
        arq.fecharArquivo();
        arq.abrirLeitura();

        copia.fecharArquivo();
        copia.abrirEscrita();

        boolean excluiu = false;
        String linha = arq.lerLinha();
        while (linha != null) {
            try {
                int cod = Integer.parseInt(piece(linha, ",", 0));
                if (cod == codExcluir) {
                    excluiu = true;
                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException ex) {
                excluiu = false;
                break;
            }
            linha = arq.lerLinha();
        }

        arq.fecharArquivo();
        copia.fecharArquivo();
        if (excluiu) {
            if (arq.deletarArquivo()) {
                copia.renomearPara(arq.getNome());
                System.out.println("Filme excluído com sucesso.");
            }
        } else {
            System.out.println("Erro ao excluir filme.");
        }
    }

    private static void cancelarIngresso(Arquivo arq, int codExcluir, Arquivo copia) {
        arq.fecharArquivo();
        arq.abrirLeitura();

        copia.fecharArquivo();
        copia.abrirEscrita();

        boolean excluiu = false;
        String linha = arq.lerLinha();
        while (linha != null) {
            try {
                int cod = Integer.parseInt(piece(linha, ",", 0));
                if (cod == codExcluir) {
                    excluiu = true;
                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException ex) {
                excluiu = false;
                break;
            }
            linha = arq.lerLinha();
        }

        arq.fecharArquivo();
        copia.fecharArquivo();
        if (excluiu) {
            if (arq.deletarArquivo()) {
                copia.renomearPara(arq.getNome());
                System.out.println("Ingresso cancelado com sucesso.");
            }
        } else {
            System.out.println("Erro ao cancelar ingresso.");
        }
    }

    private static void editarSala(Arquivo arq, int codEditar, Arquivo copia) {
        arq.fecharArquivo();
        arq.abrirLeitura();

        copia.fecharArquivo();
        copia.abrirEscrita();

        boolean editou = false;
        String linha = arq.lerLinha();
        while (linha != null) {
            try {
                int cod = Integer.parseInt(piece(linha, "#", 0));
                if (cod == codEditar) {
                    String nome = Entrada.leiaString("Novo nome da sala:");
                    int horario = Entrada.leiaInt("Novo horário:");
                    int classificacao = Entrada.leiaInt("Nova classificação:");
                    double valor = Entrada.leiaDouble("Novo valor:");
                    copia.escreverLinha(cod + "," + nome + "," + horario + "," + classificacao + "," + valor);
                    editou = true;
                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException ex) {
                editou = false;
                break;
            }
            linha = arq.lerLinha();
        }

        arq.fecharArquivo();
        copia.fecharArquivo();
        if (editou) {
            if (arq.deletarArquivo()) {
                copia.renomearPara(arq.getNome());
                System.out.println("Filme editado com sucesso.");
            }
        } else {
            System.out.println("Erro ao editar filme.");
        }
    }

    private static void excluirSala(Arquivo arq, int codExcluir, Arquivo copia) {
        arq.fecharArquivo();
        arq.abrirLeitura();

        copia.fecharArquivo();
        copia.abrirEscrita();

        boolean excluiu = false;
        String linha = arq.lerLinha();
        while (linha != null) {
            try {
                int cod = Integer.parseInt(piece(linha, "#", 0));
                if (cod == codExcluir) {
                    excluiu = true;
                } else {
                    copia.escreverLinha(linha);
                }
            } catch (NumberFormatException ex) {
                excluiu = false;
                break;
            }
            linha = arq.lerLinha();
        }

        arq.fecharArquivo();
        copia.fecharArquivo();
        if (excluiu) {
            if (arq.deletarArquivo()) {
                copia.renomearPara(arq.getNome());
                System.out.println("Sala excluída com sucesso.");
            }
        } else {
            System.out.println("Erro ao excluir sala.");
        }
    }

    private static boolean achaNoVetor(String[] vetor, int cod, String delim) {
        int aux;
        String linha;
        for (int i = 0; i < vetor.length; i++) {
            linha = vetor[i];
            if (linha == null || linha.trim().equals("")) {
                continue;
            }

            try {
                aux = Integer.parseInt(piece(linha, delim, 0));
                if (cod == aux) {
                    return true;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        return false;
    }

    private static int contaLinhas(Arquivo arq) {
        arq.fecharArquivo();
        arq.abrirLeitura();
        int count = 0;
        while (arq.lerLinha() != null) {
            count++;
        }
        arq.fecharArquivo();
        return count;
    }

    private static void atualizaVetor(String[] vetor, Arquivo arq) {
        int i = 0;
        arq.fecharArquivo();
        arq.abrirLeitura();
        String linha = arq.lerLinha();

        while (linha != null) {
            if (linha.trim().equals("")) {
                continue;
            }
            if (i >= vetor.length) {
                break;
            }
            vetor[i] = linha;
            linha = arq.lerLinha();
            i++;
        }

        arq.fecharArquivo();
    }

    private static String piece(String linha, String sep, int pos) {
        if (linha == null || sep == null || pos < 0) {
            return "";
        }
        String[] partes = linha.split(sep);
        return pos < partes.length ? partes[pos].trim() : "";
    }

    private static String imprimeVetor(String[] vetor, boolean incluiPosicao) {
        String str = "";
        if (vetor == null || vetor.length == 0) {
            return str;
        }

        String aux;
        for (int i = 0; i < vetor.length; i++) {
            aux = vetor[i];
            if (aux == null || aux.trim().equals("")) {
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

    private static void imprimirFilmes(String[] vetor) {
        if (vetor.length <= 0) {
            System.out.println("Nenhum filme no catálogo.");
            return;
        }

        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        System.out.println("\n=== FILMES NO CATÁLOGO ===");
        for (int i = 0; i < vetor.length; i++) {
            String linha = vetor[i];
            if (linha == null) {
                continue;
            }

            String cod = piece(linha, ",", 0);
            String nome = piece(linha, ",", 1);
            String horario = piece(linha, ",", 2);
            String sala = piece(linha, ",", 3);
            String classificacao = piece(linha, ",", 4);
            String valor = piece(linha, ",", 5);

            try {
                LocalTime horaFormatada = LocalTime.parse(horario);
                horario = horaFormatada.format(formatoHora);
            } catch (DateTimeParseException ex) {
            }

            try {
                double preco = Double.parseDouble(valor);
                valor = formatoMoeda.format(preco);
            } catch (NumberFormatException ex) {
            }

            try {
                int aux = Integer.parseInt(classificacao);
                classificacao = aux + " anos";
            } catch (NumberFormatException ex) {
                classificacao = "Livre";
            }

            System.out.println("Filme " + cod + " | Nome: " + nome + " | Horário: " + horario + "h Sala: " + sala + " | Classificação: " + classificacao + " | " + valor);
        }
    }

    private static void imprimirSalas(String[] vetor) {
        if (vetor.length <= 0) {
            System.out.println("Nenhuma salas no catálogo.");
            return;
        }

        System.out.println("\n=== SALAS DE CINEMA ===");
        for (int i = 0; i < vetor.length; i++) {
            String linha = vetor[i];
            if (linha == null || linha.trim().isEmpty()) {
                continue;
            }

            String codSala = piece(linha, "#", 0);
            String assentosBrutos = piece(linha, "#", 1);

            System.out.println("\nSala " + codSala + ":");

            String[] assentos = assentosBrutos.split(",");
            int contador = 0;

            for (int j = 0; j < assentos.length; j++) {
                String codAssento = piece(assentos[j], "-", 0);
                String situacao = piece(assentos[j], "-", 1);
                String linhaAssento = codAssento + " " + situacao + "  ";

                System.out.print(linhaAssento);
                contador++;

                if (contador == 5) {
                    System.out.println();
                    contador = 0;
                }
            }

            System.out.println();
        }
    }

}
