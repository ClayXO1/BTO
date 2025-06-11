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

            String sql = "INSERT INTO tecnico (nome) VALUES (?)";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nome);

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
            System.out.println("2. Voltar ao menu anterior");
            int entrada = scan.nextInt();
            scan.nextLine();

            String sql = null;

            switch (entrada) {
                case 1:
                    System.out.print("Digite o novo nome do técnico (máximo 50 caracteres): ");
                    String nome = scan.nextLine();
                    if (nome.length() > 50) {
                        throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                    }
                    sql = "UPDATE tecnico SET nome = ? WHERE id = ?";
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void mostrarTecnicos(Connection conn) {

        String sql = "SELECT * FROM tecnico";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nTécnicos cadastrados:");
            System.out.println("-------------------------------------------------------------------");
            System.out.printf("%-5s %-30s\n", "ID", "Nome");
            System.out.println("-------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                System.out.printf("%-5s %-30s\n", id, nome);
            }
            System.out.println("-------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao mostrar técnicos: " + e.getMessage());
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
        System.out.println("VOCÊ TEM CERTEZA QUE GOSTARIA DE DELETAR " + id +"? (s/n)");
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
