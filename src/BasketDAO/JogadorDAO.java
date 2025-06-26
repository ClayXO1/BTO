package BasketDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Scanner;

public class JogadorDAO {

    public static void cadastrarJogador(Connection conn, Scanner scan) {

        String nome = null;
        LocalDate dataNacimento = null;
        int timeId = 0;
        int nacionalidadeId = 0;
        int posicaoId = 0;

        while (true) {
            try {
                System.out.print("Digite o nome do jogador (máximo 50 caracteres): ");
                nome = scan.nextLine();
                if (nome.length() > 50) {
                    throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        while (true) {
            try {
                System.out.print("Digite a data de nascimento do jogador (formato: AAAA-MM-DD): ");
                dataNacimento = LocalDate.parse(scan.nextLine());
                if (dataNacimento.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("A data de nascimento não pode ser uma data futura.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        while (true) {
            try {
                listarOpcoes(conn, "time");
                System.out.println("Selecione o ID do time: ");
                
                timeId = scan.nextInt();
                if (timeId <= 0) {
                    throw new IllegalArgumentException("O ID do time deve ser um número positivo.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        while (true) {
            try {
                listarOpcoes(conn, "nacionalidade");
                System.out.println("Selecione o ID da nacionalidade: ");
                
                nacionalidadeId = scan.nextInt();
                if (nacionalidadeId <= 0) {
                    throw new IllegalArgumentException("O ID da nacionalidade deve ser um número positivo.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        while (true) {
            try {
                listarOpcoes(conn, "posicao");
                System.out.println("Selecione o ID da posição: ");
                posicaoId = scan.nextInt();
                if (posicaoId <= 0) {
                    throw new IllegalArgumentException("O ID da posição deve ser um número positivo.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        String sql = "CALL inserir_jogador(?, ?, ?, ?, ?, ?, ?)";

        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, nome);
            cs.setDate(2, java.sql.Date.valueOf(dataNacimento));
            cs.setInt(3, timeId);
            cs.setInt(4, nacionalidadeId);
            cs.setInt(5, posicaoId);
            cs.setInt(6, timeId);

            cs.registerOutParameter(7, Types.VARCHAR);

            cs.executeUpdate();

            String mensagem = cs.getString(7);
            System.out.println(mensagem);
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar jogador: " + e.getMessage());
        }
    }

    public static void  alterarJogador(Connection conn, Scanner scan) {
        try {
            mostrarJogadores(conn);
            System.out.println("Digite o ID do jogador que deseja alterar:");
            int jogadorId = scan.nextInt();
            scan.nextLine();

            System.out.println("Selecione a informação que deseja alterar:");
            System.out.println("1. Nome");
            System.out.println("2. Idade");
            System.out.println("3. Time");
            System.out.println("4. Nacionalidade");
            System.out.println("5. Posição");
            System.out.println("6. Voltar ao menu anterior");
            int entrada = scan.nextInt();
            scan.nextLine();

            String sql = null;

            switch (entrada) {
                case 1:
                    System.out.print("Digite o novo nome: ");
                    String novoNome = scan.nextLine();
                    sql = "UPDATE jogador SET nome = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, novoNome);
                        ps.setInt(2, jogadorId);
                        executarAtualizacao(ps);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Nome inválido. Deve ter no máximo 50 caracteres.");
                    }
                    break;

                case 2:
                    System.out.print("Digite a data de Nascimento: ");
                    LocalDate novaDataNascimento = LocalDate.parse(scan.nextLine());
                    if (novaDataNascimento.isAfter(LocalDate.now())) {
                        throw new IllegalArgumentException("A data de nascimento não pode ser uma data futura.");
                    } else {
                        sql = "UPDATE jogador SET data_nascimento = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setDate(1, java.sql.Date.valueOf(novaDataNascimento));
                            ps.setInt(2, jogadorId);
                            executarAtualizacao(ps);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Data de Nascimento inválida.");
                        }
                    }
                    break;

                case 3:
                    System.out.println("Selecione o ID do novo time: ");
                    listarOpcoes(conn, "time");
                    int novoTimeId = scan.nextInt();
                    sql = "UPDATE jogador SET time_id = ?, tecnico = (SELECT time.tecnico_id FROM time WHERE id = ?) WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novoTimeId);
                        ps.setInt(2, novoTimeId);
                        ps.setInt(3, jogadorId);
                        executarAtualizacao(ps);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ID do time inválido. Deve ser um número positivo.");
                    }
                    break;

                case 4:
                    System.out.println("Selecione o ID da nova nacionalidade: ");
                    listarOpcoes(conn, "nacionalidade");
                    int novaNacionalidadeId = scan.nextInt();
                    scan.nextLine();
                    sql = "UPDATE jogador SET nacionalidade_id = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novaNacionalidadeId);
                        ps.setInt(2, jogadorId);
                        executarAtualizacao(ps);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ID da nacionalidade inválido. Deve ser um número positivo.");
                    }
                    break;

                case 5:
                    System.out.println("Selecione o ID da nova posição: ");
                    listarOpcoes(conn, "posicao");
                    int novaPosicaoId = scan.nextInt();
                    scan.nextLine();
                    sql = "UPDATE jogador SET posicao_id = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novaPosicaoId);
                        ps.setInt(2, jogadorId);
                        executarAtualizacao(ps);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ID da posição inválido. Deve ser um número positivo.");
                    }
                    break;

                case 6:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao alterar jogador: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void mostrarJogadores(Connection conn) {
        String sql = "SELECT * FROM vw_jogadores";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nLista de jogadores cadastrados:");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s %-30s %-20s %-30s %-15s\n", "ID", "Nome", "Idade", "Time", "Posição", "Nacionalidade", "Técnico");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("jogador_id");
                String nome = rs.getString("jogador_nome");
                int idade = rs.getInt("idade_jogador");
                String time = rs.getString("time_nome");
                String posicao = rs.getString("posicao");
                String nacionalidade = rs.getString("nacionalidade");
                String tecnico = rs.getString("tecnico_nome");

                System.out.printf("%-5d %-20s %-10d %-30s %-20s %-30s %-15s\n", id, nome, idade, time, posicao, nacionalidade, tecnico);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao exibir jogadores: " + e.getMessage());
        }
    }

    public static void deletarJogador(Connection conn, Scanner scan) {
        mostrarJogadores(conn);
        System.out.print("Digite o ID do jogador que deseja deletar: ");
        int id = scan.nextInt();
        scan.nextLine();

        if (id <= 0) {
            System.out.println("ID inválido. Por favor, insira um ID válido.");
            return;
        }
        System.out.println("Você tem certeza que deseja deletar o jogador com ID " + id + "? (s/n)");
        String confirmacao = scan.nextLine();

        if (confirmacao.isEmpty()) {
            System.out.println("Confirmação não pode ser vazia. Por favor, responda com 's' para sim ou 'n' para não.");
            return;
        }

        if (!confirmacao.equalsIgnoreCase("s") && !confirmacao.equalsIgnoreCase("n")) {
            System.out.println("Opção inválida. Por favor, responda com 's' para sim ou 'n' para não.");
            return;
        }
        
        String sql = "DELETE FROM jogador WHERE id = ?";

        switch (confirmacao.toLowerCase()) {
            case "s":
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Jogador deletado com sucesso!");
                    } else {
                        System.out.println("Erro ao deletar jogador.");
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao deletar jogador: " + e.getMessage());
                }
                break;
            case "n":
                System.out.println("Operação cancelada. O jogador não foi deletado.");
                return;
            default:
                throw new AssertionError();
        }
    }

    private static void executarAtualizacao(PreparedStatement ps) throws SQLException {
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Informação do jogador atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar as informações do jogador.");
        }
    }

    private static void listarOpcoes(Connection conn, String tabela) {
        String sql = "SELECT id, nome FROM " + tabela;

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("Opções disponíveis na tabela " + tabela + ":");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar opções: " + e.getMessage());
        }
    }

}
