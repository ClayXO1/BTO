package BasketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TimeDAO {

    public static void cadastrarTime(Connection conn, Scanner scan) {
        String nome = null;
        String cidade = null;
        int tecnicoId = 0;

        while (true) {
            try {
                System.out.print("Digite o nome do time (máximo 50 caracteres): ");
                nome = scan.nextLine();
                if (nome.length() > 50) {
                    throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        while (true) {
            try {
                System.out.print("Digite a cidade do time (máximo 50 caracteres): ");
                cidade = scan.nextLine();
                if (cidade.length() > 50) {
                    throw new IllegalArgumentException("O nome da cidade excede o limite de 50 caracteres.");
                }
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String sql = "INSERT INTO time (nome, cidade, tecnico_id) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cidade);
            ps.setInt(3, tecnicoId);

            System.out.println("Selecione o ID do técnico:");
            listarOpcoes(conn, "tecnico");
            int novoTecnicoId = scan.nextInt();
            ps.setInt(3, novoTecnicoId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Time cadastrado com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar time.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarTimes(Connection conn) {

        //Fazer uma view para exibir time_id, nome, cidade e tecnico_nome
        // Exemplo de SQL para criar a view:
        // String createViewSQL = "CREATE VIEW vw_times AS SELECT t.id AS time_id, t.nome AS time_nome, t.cidade AS time_cidade, te.nome AS tecnico_nome " +
        //        "FROM time t JOIN tecnico te ON t.tecnico_id = te.id";
        // try (Statement st = conn.createStatement()) {
        //     st.executeUpdate(createViewSQL);
        // } catch (SQLException e) {
        //   e.printStackTrace();
        // }
        String sql = "SELECT id, nome, cidade, tecnico_id FROM time";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nTimes cadastrados:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cidade = rs.getString("cidade");
                int tecnico_id = rs.getInt("tecnico_id");

                System.out.println("ID: " + id + " | Nome: " + nome + " | Cidade: " + cidade + " | Tecnico: " + tecnico_id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void alterarTime(Connection conn, Scanner scan) {
        try {
            mostrarTimes(conn);
            System.out.print("Digite o ID do time a ser alterado: ");
            int id = scan.nextInt();
            scan.nextLine();

            System.out.println("Selecione a informação que deseja alterar:");
            System.out.println("1. Nome");
            System.out.println("2. Cidade");
            System.out.println("3. Técnico");
            System.out.println("4. Voltar ao menu anterior");
            int entrada = scan.nextInt();
            scan.nextLine();

            String sql = null;
            switch (entrada) {
                case 1:
                    System.out.print("Digite o novo nome do time: ");
                    String novoNome = scan.nextLine();
                    sql = "UPDATE time SET nome = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, novoNome);
                        ps.setInt(2, id);
                        executarAtualizacao(ps);
                    }
                    break;
                case 2:
                    System.out.print("Digite a nova cidade do time: ");
                    String novaCidade = scan.nextLine();
                    sql = "UPDATE time SET cidade = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, novaCidade);
                        ps.setInt(2, id);
                        executarAtualizacao(ps);
                    }
                    break;
                case 3:
                    System.out.println("Selecione o ID do novo técnico:");
                    listarOpcoes(conn, "tecnico");
                    int novoTecnicoId = scan.nextInt();
                    sql = "UPDATE time SET tecnico_id = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novoTecnicoId);
                        executarAtualizacao(ps);
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
