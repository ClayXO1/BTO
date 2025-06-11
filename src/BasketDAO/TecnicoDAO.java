package BasketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TecnicoDAO {

    public static void cadastrarTecnico(Connection conn, Scanner scan) {
        String nome = null;
        
         try {
                System.out.print("Digite o nome do técnico (máximo 50 caracteres): ");
                nome = scan.nextLine();
                if (nome.length() > 50) {
                    throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                }

                System.out.println("Selecione o ID do time:");
                listarOpcoes(conn, "time"); 
                int timeId = scan.nextInt();
                
                String sql = "INSERT INTO tecnico (nome,time_ID) VALUES (?, ?)";
                 
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, nome);
                    ps.setInt(2, timeId);

                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                    System.out.println("Técnico cadastrado com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar técnico.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
    } 


    public static void alterarTecnico(Connection conn, Scanner scan) {
        try {
            mostrarTecnicos(conn);
            System.out.print("Digite o ID do técnico a ser alterado: ");
            int id = scan.nextInt();
            scan.nextLine();

            System.out.println("Selecione a informação que deseja alterar:");
            System.out.println("1. Nome");
            System.out.println("2. Time");
            int entrada = scan.nextInt();
            scan.nextLine();

            String sql = null;

            switch(entrada){
                case 1:
                    System.out.print("Digite o novo nome do técnico (máximo 50 caracteres): ");
                    String nome = scan.nextLine();
                    if (nome.length() > 50) {
                        throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                    }
                    sql = "UPDATE tecnico SET nome = ? WHERE id = ?";
                    break;
                case 2:
                    System.out.println("Selecione o ID do novo time:");
                    listarOpcoes(conn, "time");
                    int timeId = scan.nextInt();
                    sql = "UPDATE tecnico SET time_ID = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, timeId);
                        ps.setInt(2, id);
                        executarAtualizacao(ps);
                    }
                    break;
                case 3:
                    return;          
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }    
    }


    public static void mostrarTecnicos(Connection conn) {
        String sql = """
                SELECT
                    tecnico.id AS id,
                    tecnico.nome AS nome,
                    time.nome AS time_nome
                FROM tecnico
                JOIN time ON tecnico.time_ID = time.id
            """;

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nTécnicos cadastrados:");
            System.out.println("-------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s\n","ID", "Nome", "Time");
            System.out.println("-------------------------------------------------------------------");
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String time_nome = rs.getString("time_nome");

                System.out.printf("%-5s %-30s %-20s\n", id, nome, time_nome);
            }
            System.out.println("-------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao mostrar técnicos: " + e.getMessage());
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


    private static void executarAtualizacao(PreparedStatement ps) throws SQLException {
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Informação do jogador atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar as informações do jogador.");
        }
    }


    public static void deletarTecnico(Connection conn, Scanner scan) {
        mostrarTecnicos(conn);
        System.out.print("Digite o ID do técnico que deseja deletar: ");
        int id = scan.nextInt();
        scan.nextLine(); 
        
        String sql = "DELETE FROM tecnico WHERE id = ?";

        if (id <= 0) {
            System.out.println("ID inválido. Por favor, insira um ID válido.");
            return;
        }
        System.out.println("Você tem certeza que deseja deletar o técnico com ID " + id + "? (s/n)");
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
                        System.out.println("Técnico deletado com sucesso!");
                    } else {
                        System.out.println("Erro ao deletar técnico.");
                    }
                } catch (SQLException e) {
                    System.out.println("Erro ao deletar técnico: " + e.getMessage());
                }
                break;
            case "n":
                System.out.println("Operação cancelada. O técnico não foi deletado.");
                return;
            default:
                throw new AssertionError();
        }
    }
}