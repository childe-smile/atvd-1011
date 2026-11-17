import java.util.Scanner;

public class SolucaoProva {

    // Constantes dadas no enunciado
    public static final int TAM_CONTAS = 50;
    public static final int TAM_OP = 1000;

    // Classe Conta conforme especificado
    public static class Conta {
        int id;
        String cliente;
        double saldo;
        double limite;

        public Conta() {}

        public Conta(int id, String cliente, double saldo, double limite) {
            this.id = id;
            this.cliente = cliente;
            this.saldo = saldo;
            this.limite = limite;
        }
    }

    // Classe Operacao conforme especificado
    public static class Operacao {
        int id;
        int idConta;
        char tipo; // 'D', 'S', 'T'
        double valor;

        public Operacao() {}

        public Operacao(int id, int idConta, char tipo, double valor) {
            this.id = id;
            this.idConta = idConta;
            this.tipo = tipo;
            this.valor = valor;
        }
    }

    // Função auxiliar dada no enunciado
    public static int buscaId(Conta[] v, int tam, int x) {
        for (int i = 0; i < tam; i++) {
            if (v[i] != null && v[i].id == x) {
                return i;
            }
        }
        return -1;
    }

    // Questão 1 (1,5 ponto)
    public static int cadastrarConta(Conta[] v, int tam) {
        if (tam >= TAM_CONTAS) {
            System.out.println("Erro: Vetor de contas está cheio!");
            return tam;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- CADASTRO DE NOVA CONTA ---");

        int id;
        while (true) {
            System.out.print("Digite o ID da conta: ");
            id = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha

            if (buscaId(v, tam, id) != -1) {
                System.out.println("Erro: ID já cadastrado! Digite outro ID.");
            } else {
                break;
            }
        }

        System.out.print("Digite o nome do cliente: ");
        String cliente = scanner.nextLine();

        double saldo;
        while (true) {
            System.out.print("Digite o saldo inicial (>= 0): ");
            saldo = scanner.nextDouble();
            if (saldo < 0) {
                System.out.println("Erro: O saldo não pode ser negativo!");
            } else {
                break;
            }
        }

        double limite;
        while (true) {
            System.out.print("Digite o limite da conta (>= 0): ");
            limite = scanner.nextDouble();
            if (limite < 0) {
                System.out.println("Erro: O limite não pode ser negativo!");
            } else {
                break;
            }
        }

        Conta novaConta = new Conta(id, cliente, saldo, limite);
        v[tam] = novaConta;

        System.out.println("Conta cadastrada com sucesso!");
        return tam + 1;
    }

    // Questão 2 (1,0 ponto)
    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
        int inicio = 0;
        int fim = tam - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comparacao = v[meio].cliente.compareToIgnoreCase(x);

            if (comparacao == 0) {
                return meio; // Encontrou o cliente
            } else if (comparacao < 0) {
                inicio = meio + 1; // Busca na metade direita
            } else {
                fim = meio - 1; // Busca na metade esquerda
            }
        }

        return -1; // Não encontrou
    }

    // Questão 3 (1,0 ponto)
    public static int insertionSortCliente(Conta[] v, int tam) {
        for (int i = 1; i < tam; i++) {
            Conta chave = v[i];
            int j = i - 1;

            while (j >= 0 && v[j].cliente.compareToIgnoreCase(chave.cliente) > 0) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = chave;
        }

        return tam;
    }

    // Questão 4 (1,0 ponto)
    public static int filtraOperacoes(Operacao[] vOp, int tamOp, Operacao[] vOpFiltrado, int x) {
        int tamFiltrado = 0;

        for (int i = 0; i < tamOp; i++) {
            if (vOp[i] != null && vOp[i].idConta == x) {
                vOpFiltrado[tamFiltrado] = vOp[i];
                tamFiltrado++;
            }
        }

        return tamFiltrado;
    }

    // Questão 5 (1,5 ponto)
    public static void extrato(Conta[] vContas, int tamConta, Operacao[] vOp, int tamOp) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDigite o ID da conta para gerar o extrato: ");
        int idConta = scanner.nextInt();

        int posConta = buscaId(vContas, tamConta, idConta);
        if (posConta == -1) {
            System.out.println("Erro: Conta não encontrada!");
            return;
        }

        Conta conta = vContas[posConta];

        // Vetor auxiliar para armazenar as operações da conta
        Operacao[] opsConta = new Operacao[tamOp];
        int numOps = filtraOperacoes(vOp, tamOp, opsConta, idConta);

        // Armazenar históricos de saldos acumulados
        double[] saldosAposOp = new double[numOps];
        double saldoAcumulado = 0.0; // Saldo inicial zero conforme enunciado

        for (int i = 0; i < numOps; i++) {
            Operacao op = opsConta[i];
            // Depósito ou valor positivo incrementa saldo; Saque/Transferência ou valor negativo decrementa
            if (op.tipo == 'D' || (op.tipo != 'S' && op.tipo != 'T' && op.valor > 0)) {
                saldoAcumulado += Math.abs(op.valor);
            } else {
                saldoAcumulado -= Math.abs(op.valor);
            }
            saldosAposOp[i] = saldoAcumulado;
        }

        // Determinar o ponto de início das últimas 5 operações
        int inicio = Math.max(0, numOps - 5);
        double saldoAnterior = (inicio > 0) ? saldosAposOp[inicio - 1] : 0.0;

        // Impressão do cabeçalho do Extrato
        System.out.println("+----------------------------------------+");
        System.out.printf("| C O N T A  N.%03d                      |\n", conta.id);
        System.out.println("+----------------------------------------+");
        System.out.printf("|CLIENTE: %-31s|\n", conta.cliente);
        System.out.println("+----+-----------------+--------+--------+");
        System.out.println("| ID | O P E R A Ç Ã O | VALOR  | SALDO  |");
        System.out.println("+----+-----------------+--------+--------+");
        System.out.printf("|    |Saldo Anterior   |        |%7.2f%s|\n",
                Math.abs(saldoAnterior), (saldoAnterior >= 0 ? "C" : "D"));

        // Impressão das últimas 5 operações
        for (int i = inicio; i < numOps; i++) {
            Operacao op = opsConta[i];
            String nomeOp;
            boolean ehCredito;

            if (op.tipo == 'D') {
                nomeOp = "Depósito";
                ehCredito = true;
            } else if (op.tipo == 'S') {
                nomeOp = "Saque";
                ehCredito = false;
            } else {
                nomeOp = "Transferência";
                ehCredito = (op.valor > 0);
            }

            double valExibicao = Math.abs(op.valor);
            char tipoVal = ehCredito ? 'C' : 'D';

            double saldoApos = saldosAposOp[i];
            char tipoSaldo = (saldoApos >= 0) ? 'C' : 'D';

            System.out.printf("|%04d|%-17s|%7.2f%c|%7.2f%c|\n",
                    op.id, nomeOp, valExibicao, tipoVal, Math.abs(saldoApos), tipoSaldo);
        }

        System.out.println("+----+-----------------+--------+--------+");
        double saldoFinal = (numOps > 0) ? saldosAposOp[numOps - 1] : 0.0;
        System.out.printf("| Saldo Atual                  |%7.2f%s|\n",
                Math.abs(saldoFinal), (saldoFinal >= 0 ? "C" : "D"));
        System.out.println("+----------------------------------------+\n");
    }

    // Método main para testar toda a implementação
    public static void main(String[] args) {
        Conta[] contas = new Conta[TAM_CONTAS];
        int tamContas = 0;

        Operacao[] operacoes = new Operacao[TAM_OP];
        int tamOp = 0;

        // Inserção de dados de teste (semelhantes ao exemplo do enunciado)
        contas[tamContas++] = new Conta(1, "Maria José Santos", 424.99, 1000.0);
        contas[tamContas++] = new Conta(2, "João Silva", 1500.0, 500.0);
        contas[tamContas++] = new Conta(3, "Ana Costa", 800.0, 2000.0);

        // Operações da Maria José Santos (idConta = 1) acumulando o histórico do exemplo
        operacoes[tamOp++] = new Operacao(10, 1, 'D', 2437.55); // Gera Saldo Ant = 2437.55C
        operacoes[tamOp++] = new Operacao(25, 1, 'D', 1000.00); // Saldo = 3437.55C
        operacoes[tamOp++] = new Operacao(37, 1, 'D', 237.44);  // Saldo = 3674.99C
        operacoes[tamOp++] = new Operacao(83, 1, 'T', -4000.00); // Saldo = 325.01D
        operacoes[tamOp++] = new Operacao(95, 1, 'S', 250.00);   // Saldo = 575.01D
        operacoes[tamOp++] = new Operacao(112, 1, 'T', 1000.00); // Saldo = 424.99C

        System.out.println("=== TESTE DE ORDENAÇÃO E BUSCA ===");
        insertionSortCliente(contas, tamContas);
        System.out.println("Contas ordenadas por nome:");
        for (int i = 0; i < tamContas; i++) {
            System.out.println("ID: " + contas[i].id + ", Nome: " + contas[i].cliente);
        }

        int pos = buscaBinariaCliente(contas, tamContas, "Maria José Santos");
        System.out.println("\nBusca Binária por 'Maria José Santos': encontrada na posição " + pos);

        System.out.println("\n=== TESTE DE EXTRATO ===");
        // Testando o extrato diretamente simulando entrada
        extrato(contas, tamContas, operacoes, tamOp);
    }
}
