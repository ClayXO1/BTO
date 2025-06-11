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

        String sql = "INSERT INTO time (nome, cidade) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, cidade);

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
        String sql = "SELECT id, nome, cidade FROM time";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nTimes cadastrados:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String cidade = rs.getString("cidade");

                System.out.println("ID: " + id + " | Nome: " + nome + " | Cidade: " + cidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
