
import BasketDAO.JogadorDAO;
import BasketDAO.NacionalidadeDAO;
import BasketDAO.PartidaDAO;
import BasketDAO.PosicaoDAO;
import BasketDAO.TecnicoDAO;
import BasketDAO.TimeDAO;
import Conexao.Conexao;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Connection conn = Conexao.getConexao()) {
            if (conn == null) {
                System.out.println("Falha ao conectar com o banco de dados.");
                return;
            }

            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("//////////////////////////////////////////////////////////");
                System.out.println("//                  MENU PRINCIPAL                      //");
                System.out.println("//////////////////////////////////////////////////////////");
                System.out.println("1. Gerenciar Jogadores");
                System.out.println("2. Gerenciar Tecnicos");
                System.out.println("3. Gerenciar Times");
                System.out.println("4. Gerenciar Posições");
                System.out.println("5. Gerenciar Nacionalidades");
                System.out.println("6. Gerenciar Partidas");
                System.out.println("7. Sair");
                System.out.print("Escolha uma opção: ");
                int entrada = scan.nextInt();
                scan.nextLine();

                switch (entrada) {
                    case 1:
                        menuJogadores(conn, scan);
                        break;
                    case 2:
                        menuTecnicos(conn, scan);
                        break;
                    case 3:
                        menuTimes(conn, scan);
                        break;
                    case 4:
                        menuPosicoes(conn, scan);
                        break;
                    case 5:
                        menuNacionalidades(conn, scan);
                        break;
                    case 6:
                        menuPartidas(conn, scan);
                        break;
                    case 7:
                        System.out.println("Encerrando o programa...");
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void menuJogadores(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Jogadores ***");
            System.out.println("1. Cadastrar Jogador");
            System.out.println("2. Mostrar Jogadores");
            System.out.println("3. Alterar informações dos jogadores");
            System.out.println("4. Deletar Jogador");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    JogadorDAO.cadastrarJogador(conn, scan);
                    break;
                case 2:
                    JogadorDAO.mostrarJogadores(conn);
                    break;
                case 3:
                    JogadorDAO.alterarJogador(conn, scan);
                    break;
                case 4:
                    JogadorDAO.deletarJogador(conn, scan);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuNacionalidades(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Nacionalidades ***");
            System.out.println("1. Cadastrar Nacionalidade");
            System.out.println("2. Mostrar Nacionalidades");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    NacionalidadeDAO.cadastrarNacionalidade(conn, scan);
                    break;
                case 2:
                    NacionalidadeDAO.mostrarNacionalidades(conn);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuTimes(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Times ***");
            System.out.println("1. Cadastrar Time");
            System.out.println("2. Mostrar Times");
            System.out.println("3. Alterar Time");
            System.out.println("4. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    TimeDAO.cadastrarTime(conn, scan);
                    break;
                case 2:
                    TimeDAO.mostrarTimes(conn);
                    break;
                case 3:
                    TimeDAO.alterarTime(conn, scan);
                    break;
                    case 4:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuPosicoes(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Posições ***");
            System.out.println("1. Cadastrar Posição");
            System.out.println("2. Mostrar Posições");
            System.out.println("3. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    PosicaoDAO.cadastrarPosicao(conn, scan);
                    break;
                case 2:
                    PosicaoDAO.mostrarPosicoes(conn);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuTecnicos(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Técnicos ***");
            System.out.println("1. Cadastrar Técnico");
            System.out.println("2. Mostrar Técnicos");
            System.out.println("3. Alterar informações dos técnicos");
            System.out.println("4. Deletar Técnico");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    TecnicoDAO.cadastrarTecnico(conn, scan);
                    break;
                case 2:
                    TecnicoDAO.mostrarTecnicos(conn);
                    break;
                case 3:
                    TecnicoDAO.alterarTecnico(conn, scan);
                    break;
                case 4:
                    TecnicoDAO.deletarTecnico(conn, scan);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuPartidas(Connection conn, Scanner scan) {
        while (true) {
            System.out.println("\n*** Gerenciamento de Partidas ***");
            System.out.println("1. Cadastrar Partida");
            System.out.println("2. Mostrar Partidas");
            System.out.println("3. Alterar Partida");
            System.out.println("4. Deletar Partida");
            System.out.println("5. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");
            int entrada = scan.nextInt();
            scan.nextLine();

            switch (entrada) {
                case 1:
                    PartidaDAO.cadastrarPartida(conn, scan);
                    break;
                case 2:
                    PartidaDAO.mostrarPartidas(conn);
                    break;
                case 3:
                    PartidaDAO.alterarPartida(conn, scan); 
                    break;
                case 4:
                    PartidaDAO.deletarPartida(conn, scan);
                    return;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
