package BasketDAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class PartidaDAO {

    public static void cadastrarPartida(Connection conn, Scanner scan) {

        int timeCasaId = 0;
        int timeVisitanteId = 0;
        LocalDate data = null;
        LocalTime hora = null;
        String local = null;
        

        while (true) {
            try {
                System.out.print("Digite o ID do time da casa: ");
                listarOpcoes(conn, "time");
                timeCasaId = scan.nextInt();
                scan.nextLine();
                if (timeCasaId <= 0) {
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
                System.out.print("Digite o ID do time visitante: ");
                listarOpcoes(conn, "time");
                timeVisitanteId = scan.nextInt();
                scan.nextLine();
                if (timeVisitanteId <= 0) {
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
                System.out.print("Digite a data da partida (formato YYYY-MM-DD): ");
                data = LocalDate.parse(scan.nextLine());
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        while(true) {
            try {
                System.out.print("Digite a hora da partida (formato HH:MM): ");
                hora = LocalTime.parse(scan.nextLine());
                break; 
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        String sql = "CALL marcar_partida(?, ?, ?, ?, ?, ?)";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setInt(1, timeCasaId);
            cs.setInt(2, timeVisitanteId);
            cs.setDate(3, java.sql.Date.valueOf(data));
            cs.setTime(4, java.sql.Time.valueOf(hora));
            cs.setInt(5, timeCasaId);
            cs.registerOutParameter(6, Types.VARCHAR);

            cs.execute();

            String mensagem = cs.getString(6);
            System.out.println(mensagem);

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar partida: " + e.getMessage());
        }
    }

    public static void alterarPartida(Connection conn,  Scanner scan){
        try{
        mostrarPartidas(conn);
            System.out.println("Digite o ID da partida que deseja alterar:");
            int partidaId = scan.nextInt();
            scan.nextLine();

            System.out.println("Selecione a informação que deseja alterar:");
            System.out.println("1. Time da Casa");
            System.out.println("2. Time Visitante");
            System.out.println("3. Data");
            System.out.println("4. Hora");
            System.out.println("5. Voltar ao menu anterior");
            int entrada = scan.nextInt();
            scan.nextLine();

            String sql = null; 

            switch (entrada) {
                case 1:
                    System.out.print("Digite o novo ID do time da casa: ");
                    int novoTimeCasaId = scan.nextInt();
                    scan.nextLine();
                    sql = "UPDATE partida SET time_casa_id = ?, partida_local = (SELECT partida_local FROM partida WHERE id = ?) WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novoTimeCasaId);
                        ps.setInt(2,novoTimeCasaId);
                        ps.setInt(3, partidaId);
                        executarAtualizacao(ps); 
                    } catch (SQLException e) {
                        System.out.println("Erro ao atualizar o time da casa: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Digite o novo ID do time visitante: ");
                    int novoTimeVisitanteId = scan.nextInt();
                    scan.nextLine();
                    sql = "UPDATE partida SET time_visitante_id = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, novoTimeVisitanteId);
                        ps.setInt(2, partidaId);
                        executarAtualizacao(ps);
                    } catch (SQLException e) {
                        System.out.println("Erro ao atualizar o time visitante: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Digite a nova data da partida (formato YYYY-MM-DD): ");
                    LocalDate novaData = LocalDate.parse(scan.nextLine());
                    sql = "UPDATE partida SET data = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setDate(1, java.sql.Date.valueOf(novaData));
                        ps.setInt(2, partidaId);
                        executarAtualizacao(ps);
                    } catch (SQLException e) {
                        System.out.println("Erro ao atualizar a data da partida: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Digite a nova hora da partida (formato HH:MM): ");
                    LocalTime novaHora = LocalTime.parse(scan.nextLine());
                    sql = "UPDATE partida SET hora = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setTime(1, java.sql.Time.valueOf(novaHora));
                        ps.setInt(2, partidaId);
                        executarAtualizacao(ps);
                    } catch (SQLException e) {
                        System.out.println("Erro ao atualizar a hora da partida: " + e.getMessage());
                    } 
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

            
        } catch (IllegalArgumentException e) {
            System.out.println("Entrada inválida: " + e.getMessage());
        }

    }

    public static void deletarPartida(Connection conn, Scanner scan) {
        mostrarPartidas(conn);
        System.out.print("Digite o ID da partida que deseja excluir: ");
        int partidaId = scan.nextInt();
        scan.nextLine();

        String sql = "DELETE FROM partida WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Partida excluída com sucesso!");
            } else {
                System.out.println("Nenhuma partida encontrada com o ID fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao excluir partida: " + e.getMessage());
        }
    }

    public static void mostrarPartidas(Connection conn) {
        String sql = "SELECT * FROM vw_partidas";

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nLista de partidas cadastradas:");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-20s %-30s %-30s %-30s %-20s %-15s\n", "ID", "Time Casa", "Time Visitante", "Data", "Hora", "Local");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("partida_id");
                String timeCasa = rs.getString("time_casa");
                String timeVisitante = rs.getString("time_visitante");
                LocalDate data = rs.getDate("partida_data").toLocalDate();
                LocalTime hora = rs.getTime("partida_hora").toLocalTime();
                String local = rs.getString("partida_local");

                System.out.printf("%-20d %-30s %-30s %-30s %-20s %-15s\n", id, timeCasa, timeVisitante, data, hora, local);
            }
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } catch (SQLException e) {
            System.out.println("Erro ao exibir partidas: " + e.getMessage());
        }
    }

    private static void executarAtualizacao(PreparedStatement ps) throws SQLException {
        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Informação da partida atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar as informações da partida.");
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


