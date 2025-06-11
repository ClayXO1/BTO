package BasketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PosicaoDAO {

    public static void cadastrarPosicao(Connection conn, Scanner scan) {
        String nome = null;

        while (true) {
            try {
                System.out.print("Digite o nome da posição (máximo 30 caracteres): ");
                nome = scan.nextLine();
                if (nome.length() > 30) {
                    throw new IllegalArgumentException("O nome excede o limite de 30 caracteres.");
                }
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String sql = "INSERT INTO posicao (nome) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Posição cadastrada com sucesso!");
            } else {
                System.out.println("Erro ao cadastrar posição.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void mostrarPosicoes(Connection conn) {
        String sql = "SELECT id, nome FROM posicao";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nPosições cadastradas:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                System.out.println("ID: " + id + " | Nome: " + nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
