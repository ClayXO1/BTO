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

        String sql = "SELECT * FROM vw_times";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
           
            System.out.println("\nLista de Times cadastrados:");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-40s %-40s %-30s\n", "ID", "Nome", "Cidade", "Técnico");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("time_id");
                String nome = rs.getString("time_nome");
                String cidade = rs.getString("time_cidade");
                String tecnico_nome = rs.getString("tecnico_nome");

                System.out.printf("%-5d %-40s %-40s %-30s\n", id, nome, cidade, tecnico_nome);
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
                    } catch (IllegalArgumentException e) {
                        System.out.println("Nome inválido. Deve ter no máximo 50 caracteres.");
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
