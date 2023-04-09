import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class Main {
    public static void main(String[] args) {
        Menu.anaMenu();
    }
}


/*Kullanıcılar elit veya genel üye olarak kaydedilir.
Programın çalışması için kullanıcıların e-posta adresleri kaydedilir ve bu adreslere
e-posta gönderme işlemi gerçekleştirilir*/
 class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static DosyaIslemleri dosyaIslemleri = new DosyaIslemleri();
    /*anaMenu fonksiyonu, kullanıcının seçimlerine göre üye ekleme veya mail gönderme işlemlerini yapar*/
    public static void anaMenu() {
        while (true) {
            System.out.println("1- Elit üye ekleme");
            System.out.println("2- Genel üye ekleme");
            System.out.println("3- Mail Gönderme");
            int secim = scanner.nextInt();
            scanner.nextLine(); // boş satırı oku
            switch (secim) {
                case 1:
                    System.out.print("Isim: ");
                    String elitIsim = scanner.nextLine();
                    System.out.print("Soyisim: ");
                    String elitSoyisim = scanner.nextLine();
                    System.out.print("Email: ");
                    String elitEmail = scanner.nextLine();
                    ElitUye elitUye = new ElitUye(elitIsim, elitSoyisim, elitEmail);
                    try {
                        dosyaIslemleri.uyeKaydet("elitUyeler.txt", elitUye);
                        System.out.println("Elit üye kaydedildi.");
                    } catch (IOException e) {
                        System.out.println("Dosya işlemleri hatası: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Isim: ");
                    String genelIsim = scanner.nextLine();
                    System.out.print("Soyisim: ");
                    String genelSoyisim = scanner.nextLine();
                    System.out.print("Email: ");
                    String genelEmail = scanner.nextLine();
                    GenelUye genelUye = new GenelUye(genelIsim, genelSoyisim, genelEmail);
                    try {
                        dosyaIslemleri.uyeKaydet("genelUyeler.txt", genelUye);
                        System.out.println("Genel üye kaydedildi.");
                    } catch (IOException e) {
                        System.out.println("Dosya işlemleri hatası: " + e.getMessage());
                    }
                    break;
                case 3:
                    mailMenu();
                    break;

                default:
                    System.out.println("Geçersiz seçim.");



            }
        }
    }
    /*mailMenu fonksiyonu, kullanıcının seçimlerine göre elit,
    genel veya tüm üyelere mail gönderme işlemi yapar*/
    public static void mailMenu() {
        while (true) {
            System.out.println("1- Elit üyelere mail");
            System.out.println("2- Genel üyelere mail");
            System.out.println("3- Tüm üyelere mail");
            System.out.println("4- Ana menüye dön");
            int secim = scanner.nextInt();
            scanner.nextLine(); // boş satırı oku
            switch (secim) {
                case 1:
                    System.out.print("Mail icerigi: ");
                    String elitIcerik = scanner.nextLine();
                    try {
                        List<Uye> elitUyeler = dosyaIslemleri.uyeleriGetir("elitUyeler.txt");
                        for (Uye uye : elitUyeler) {
                            mailGonder("soyleomer72@gmail.com", "npnusseixefbhxos", uye.getEposta(), "Elit üyeler için", elitIcerik);
                        }
                        System.out.println("Mail gönderildi.");
                    } catch (IOException e) {
                        System.out.println("Dosya işlemleri hatası: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Mail icerigi: ");
                    String genelIcerik = scanner.nextLine();
                    try {
                        List<Uye> genelUyeler = dosyaIslemleri.uyeleriGetir("genelUyeler.txt");
                        for (Uye uye : genelUyeler) {
                            mailGonder("soyleomer72@gmail.com", "npnusseixefbhxos", uye.getEposta(), "Genel üyeler için", genelIcerik);
                        }
                        System.out.println("Mail gönderildi.");
                    } catch (IOException e) {
                        System.out.println("Dosya işlemleri hatası: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.print("Mail icerigi: ");
                    String tumIcerik = scanner.nextLine();
                    try {
                        List<Uye> tumUyeler = dosyaIslemleri.uyeleriGetir("elitUyeler.txt");
                        tumUyeler.addAll(dosyaIslemleri.uyeleriGetir("genelUyeler.txt"));
                        for (Uye uye : tumUyeler) {
                            mailGonder("soyleomer72@gmail.com", "npnusseixefbhxos", uye.getEposta(), "Tüm üyeler için", tumIcerik);
                        }
                        System.out.println("Mail gönderildi.");
                    } catch (IOException e) {
                        System.out.println("Dosya işlemleri hatası: " + e.getMessage());
                    }
                case 4:
                    return; // ana menüye dön

                default:
                    System.out.println("Geçersiz seçim.");
            }
        }
    }
    /*mailGonder fonksiyonu, JavaMail API'si kullanarak mail gönderme işlemi yapar.
    Bu fonksiyon gonderenEposta ve sifre parametreleri ile giriş yapar ve mail gönderme işlemini gerçekleştirir*/
    public static void mailGonder(String gonderenEposta, String sifre, String aliciEposta, String konu, String icerik) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gonderenEposta, sifre);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(gonderenEposta));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(aliciEposta));
            message.setSubject(konu);
            message.setText(icerik);
            Transport.send(message);
            System.out.println("Mail gönderildi: " + aliciEposta);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

/*DosyaIslemleri sınıfı, dosya okuma ve yazma işlemlerini gerçekleştirir.
uyeKaydet fonksiyonu, verilen dosya ismi ve üye nesnesi ile dosyaya üye bilgilerini yazar.
uyeleriGetir fonksiyonu, verilen dosya ismi ile dosyadaki tüm üye bilgilerini
okur ve bir List<Uye> nesnesi olarak döndürür.
*/
 class DosyaIslemleri {
    public void uyeKaydet(String dosyaAdi, Uye uye) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(dosyaAdi, true));
        writer.write(uye.format() + "\n");
        writer.close();
    }

    public List<Uye> uyeleriGetir(String dosyaAdi) throws IOException {
        List<Uye> uyeler = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(dosyaAdi));
        String satir = reader.readLine();
        while (satir != null) {
            String[] bilgiler = satir.split("\t");
            if (bilgiler.length != 4) {
                reader.close();
                throw new IOException("Hatalı dosya biçimi: " + satir);
            }
            if (bilgiler[3].equals("ELIT")) {
                uyeler.add(new ElitUye(bilgiler[0], bilgiler[1], bilgiler[2]));
            } else if (bilgiler[3].equals("GENEL")) {
                uyeler.add(new GenelUye(bilgiler[0], bilgiler[1], bilgiler[2]));
            } else {
                reader.close();
                throw new IOException("Hatalı dosya biçimi: " + satir);
            }
            satir = reader.readLine();
        }
        reader.close();
        return uyeler;
    }
}
/*GenelUye sınıfı, üyelerin isim, soyisim ve e-posta bilgilerini tutar ve
bu bilgilere erişim için get/set fonksiyonları sağlar.
GenelUye Uye sınıfını extends eder.

*/
 class GenelUye extends Uye {
    public GenelUye(String ad, String soyad, String eposta) {
        super(ad, soyad, eposta);
    }

    @Override
    public String format() {
        return getAd() + "\t" + getSoyad() + "\t" + getEposta() + "\tGENEL";
    }

    @Override
    public String toString() {
        return "Genel Üye - Ad: " + getAd() + ", Soyad: " + getSoyad() + ", E-posta: " + getEposta();
    }
}
/*ElitUye sınıfı, üyelerin isim, soyisim ve e-posta bilgilerini tutar ve
bu bilgilere erişim için get/set fonksiyonları sağlar.
ElitUye Uye sınıfını extends eder.

*/
 class ElitUye extends Uye {
    public ElitUye(String ad, String soyad, String eposta) {
        super(ad, soyad, eposta);
    }

    @Override
    public String format() {
        return getAd() + "\t" + getSoyad() + "\t" + getEposta() + "\tELIT";
    }



}

 abstract class Uye {
    private String ad;
    private String soyad;
    private String eposta;

    public Uye(String ad, String soyad, String eposta) {
        this.ad = ad;
        this.soyad = soyad;
        this.eposta = eposta;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String getEposta() {
        return eposta;
    }

    public abstract String format();
}