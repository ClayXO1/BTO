package BasketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class NacionalidadeDAO {

    public static void cadastrarNacionalidade(Connection conn, Scanner scanner) {
        String nome = null;

        while (true) {
            try {
                System.out.print("Digite o nome da nacionalidade (máximo 50 caracteres): ");
                nome = scanner.nextLine();
                if (nome.length() > 50) {
                    throw new IllegalArgumentException("O nome excede o limite de 50 caracteres.");
                }
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String sql = "INSERT INTO nacionalidade (nome) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Nacionalidade cadastrada com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar nacionalidade.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarNacionalidades(Connection conn) {
        String sql = "SELECT id, nome FROM nacionalidade";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nLista de Nacionalidades cadastradas:");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-50s\n", "ID", "Nome");
            System.out.println("---------------------------------------------------------------------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                System.out.printf("%-5d %-50s\n", id, nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
