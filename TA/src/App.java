    import java.io.*;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.Scanner;

    // 1. Inheritance
    class Kendaraan {
        private String platNomor;
        private Date jamMasuk;

        public Kendaraan(String platNomor) {
            this.platNomor = platNomor;
            this.jamMasuk = new Date();
        }

        public String getPlatNomor() {
            return platNomor;
        }

        public Date getJamMasuk() {
            return jamMasuk;
        }
    }

    class Mobil extends Kendaraan {
        public Mobil(String platNomor) {
            super(platNomor);
        }
    }

    // 2. Encapsulation
    class ParkirManager {
        private ArrayList<Kendaraan> dataKendaraan;
        private ArrayList<Kendaraan> historisKendaraan;

        public ParkirManager() {
            this.dataKendaraan = new ArrayList<>();
            this.historisKendaraan = new ArrayList<>();
            loadDaftarKendaraanHistoris();
        }

        // 3. Polymorphism
        public Kendaraan checkIn(String platNomor) {
            Kendaraan kendaraan = new Mobil(platNomor);
            dataKendaraan.add(kendaraan);
            saveDaftarKendaraanHistoris(); 
            return kendaraan;
        }

        public void checkOut(Kendaraan kendaraan) {
            
            long durasi = new Date().getTime() - kendaraan.getJamMasuk().getTime();
            long biaya = hitungBiayaParkir(durasi);

            
            cetakStruk(kendaraan, biaya);

            
            historisKendaraan.add(kendaraan);
            dataKendaraan.remove(kendaraan);

            
            saveDaftarKendaraanHistoris();
        }

        private long hitungBiayaParkir(long durasi) {
            long biaya = 0;
            long sepuluhMenit = 10 * 60 * 1000; 

            if (durasi > sepuluhMenit) {
                biaya = 10000;
                durasi -= sepuluhMenit;

                biaya += (durasi / (24 * 60 * 60 * 1000)) * 5000; 
            }

            return biaya;
        }

        private void cetakStruk(Kendaraan kendaraan, long biaya) {
            System.out.println("Struk Parkir");
            System.out.println("Plat Nomor: " + kendaraan.getPlatNomor());
            System.out.println("Jam Masuk: " + kendaraan.getJamMasuk());
            System.out.println("Biaya Parkir: " + biaya);
        }

        // 4. Abstraction
        public void displayDaftarParkir() {
            System.out.println("Daftar Kendaraan yang Sedang Parkir:");

            for (Kendaraan kendaraan : dataKendaraan) {
                System.out.println("Plat Nomor: " + kendaraan.getPlatNomor());
                System.out.println("Jam Masuk: " + kendaraan.getJamMasuk());
                System.out.println("---------------------------");
            }
        }

        public void displayDaftarParkirHistoris() {
            System.out.println("Daftar Kendaraan yang Pernah Parkir:");

            for (Kendaraan kendaraan : historisKendaraan) {
                System.out.println("Plat Nomor: " + kendaraan.getPlatNomor());
                System.out.println("Jam Masuk: " + kendaraan.getJamMasuk());
                System.out.println("---------------------------");
            }
        }

        private void saveDaftarKendaraanHistoris() {
            try (PrintWriter writer = new PrintWriter(new FileWriter("historis.txt"))) {
                for (Kendaraan kendaraan : historisKendaraan) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = dateFormat.format(kendaraan.getJamMasuk());
                    writer.println(kendaraan.getPlatNomor() + "," + formattedDate);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadDaftarKendaraanHistoris() {
            try (Scanner scanner = new Scanner(new File("historis.txt"))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(",");
                    String platNomor = parts[0];
                    String formattedDate = parts[1];

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date jamMasuk = dateFormat.parse(formattedDate);

                    Kendaraan kendaraan = new Mobil(platNomor);
                    historisKendaraan.add(kendaraan);
                }
            } catch (FileNotFoundException | java.text.ParseException e) {
            }
        }

        public ArrayList<Kendaraan> getDataKendaraan() {
            return dataKendaraan;
        }
    }

    class Admin {
        private String username;
        private String password;

        public Admin(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public boolean authenticate(String enteredUsername, String enteredPassword) {
            return username.equals(enteredUsername) && password.equals(enteredPassword);
        }
    }

    class AdminLogin {
        private Admin admin;

        public AdminLogin() {
            this.admin = new Admin("admin", "admin");
        }

        public boolean login() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Login Admin");

            System.out.print("Username: ");
            String enteredUsername = scanner.next();

            System.out.print("Password: ");
            String enteredPassword = scanner.next();

            if (admin.authenticate(enteredUsername, enteredPassword)) {
                System.out.println("Login berhasil!");
                return true;
            } else {
                System.out.println("Login gagal. Coba lagi.");
                return false;
            }
        }
    }

    public class App {
        public static void main(String[] args) {
            AdminLogin adminLogin = new AdminLogin();

            // Login admin
            while (!adminLogin.login()) {
               
            }

            
            ParkirManager parkirManager = new ParkirManager();
            Scanner scanner = new Scanner(System.in);

            int choice;
            do {
                System.out.println("===============================================");
                System.out.println("                    Menu                       ");
                System.out.println("===============================================");
                System.out.println("1. Check-in kendaraan                        +");
                System.out.println("2. Check-out kendaraan                       +");
                System.out.println("3. Lihat daftar kendaraan yang sedang parkir +");
                System.out.println("4. Lihat daftar kendaraan historis           +");
                System.out.println("5. Keluar                                    +");
                System.out.println("===============================================");

                System.out.print("Masukkan pilihan : ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Masukkan plat nomor kendaraan: ");
                        String platNomor = scanner.next();
                        Kendaraan kendaraan = parkirManager.checkIn(platNomor);
                        System.out.println("Kendaraan dengan plat nomor " + platNomor + " berhasil check-in.");
                        break;
                    case 2:
                        System.out.print("Masukkan plat nomor kendaraan yang akan check-out: ");
                        platNomor = scanner.next();
                        Kendaraan kendaraanToCheckOut = findKendaraanByPlatNomor(parkirManager, platNomor);
                        if (kendaraanToCheckOut != null) {
                            parkirManager.checkOut(kendaraanToCheckOut);
                        } else {
                            System.out.println("Kendaraan dengan plat nomor " + platNomor + " tidak ditemukan.");
                        }
                        break;
                    case 3:
                        parkirManager.displayDaftarParkir();
                        break;
                    case 4:
                        parkirManager.displayDaftarParkirHistoris();
                        break;
                    case 5:
                        System.out.println("Terima kasih!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } while (choice != 5);
        }

        private static Kendaraan findKendaraanByPlatNomor(ParkirManager parkirManager, String platNomor) {
            for (Kendaraan kendaraan : parkirManager.getDataKendaraan()) {
                if (kendaraan.getPlatNomor().equals(platNomor)) {
                    return kendaraan;
                }
            }
            return null;
        }
    }
