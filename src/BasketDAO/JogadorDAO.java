package BasketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JogadorDAO {

    public static void cadastrarJogador(Connection conn, Scanner scan) {
        String nome = null;
        int idade = 0;
        int timeId = 0;
        int nacionalidadeId = 0;
        int posicaoId = 0;
        int tecnicoId = 0;

        while(true) {
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
                System.out.print("Digite a idade do jogador: ");
                idade = scan.nextInt();
                if (idade <= 0) {
                    throw new IllegalArgumentException("A idade deve ser um número positivo.");
                }
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }

        while (true) { 
            try {
                System.out.println("Selecione o ID do time: ");
                listarOpcoes(conn, "time");
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
                System.out.println("Selecione o ID da nacionalidade: ");
                listarOpcoes(conn, "nacionalidade");
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
                System.out.println("Selecione o ID da posição: ");
                listarOpcoes(conn, "posicao");
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

        String sql = "INSERT INTO JOGADOR (nome, idade, time_id, nacionalidade_id, posicao_id, tecnico) VALUES (?, ?, ?, ?, ?, (SELECT tecnico_id FROM time WHERE id = ?))";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome); 
            ps.setInt(2, idade);
            ps.setInt(3, timeId);
            ps.setInt(4, nacionalidadeId);
            ps.setInt(5, posicaoId);
            ps.setInt(6, timeId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Jogador cadastrado com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar jogador.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar jogador: " + e.getMessage());
        }
    }
    

    public static void alterarJogador(Connection conn, Scanner scan) {
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
                    System.out.print("Digite a nova idade: ");
                    int novaIdade = scan.nextInt();
                    sql = "UPDATE jogador SET idade = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novaIdade);
                        ps.setInt(2, jogadorId);
                        executarAtualizacao(ps);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Idade inválida. Deve ser um número positivo.");
                    }
                    break;

                case 3:
                    System.out.println("Selecione o ID do novo time: ");
                    listarOpcoes(conn, "time");
                    int novoTimeId = scan.nextInt();
                    sql = "UPDATE jogador SET time_id = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novoTimeId);
                        ps.setInt(2, jogadorId);
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
        String sql = "SELECT * FROM vw_jogadores_detalhado";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nLista de jogadores cadastrados:");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-5s %-20s %-20s %-15s %-15s\n", "ID", "Nome", "Idade", "Time", "Posição", "Nacionalidade", "Técnico");
            System.out.println("---------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("jogador_id");
                String nome = rs.getString("jogador_nome");
                int idade = rs.getInt("idade");
                String time = rs.getString("time_nome");
                String posicao = rs.getString("posicao_nome");
                String nacionalidade = rs.getString("nacionalidade");
                String tecnico = rs.getString("tecnico_nome");

                System.out.printf("%-5d %-20s %-5d %-20s %-20s %-15s %-15s\n", id, nome, idade, time, posicao, nacionalidade, tecnico);
            }
            System.out.println("---------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao exibir jogadores: " + e.getMessage());
        }
    }

    public static void deletarJogador(Connection conn, Scanner scan) {
        mostrarJogadores(conn);
        System.out.print("Digite o ID do jogador que deseja deletar: ");
        int id = scan.nextInt();
        scan.nextLine();

        String sql = "DELETE FROM jogador WHERE id = ?";

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
