package de.hsa.sniffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapException;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Sniffer {

    public static void main(String[] args) throws PcapException {
        List<PcapIf> devices = Pcap.findAllDevs();
        if (devices.isEmpty()) {
            System.out.println("Keine Interfaces gefunden.");
            return;
        }

        int selectedIndex = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            switch (input) {
                case "1":
                    listDevices(devices);
                    break;
                case "2":
                    selectedIndex = selectDevice(devices, scanner, selectedIndex);
                    break;
                case "3":
                    sniff(devices, selectedIndex);
                    break;
                default:
                    System.out.println("Ungueltige Auswahl.");
                    break;
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1) List devices");
        System.out.println("2) Select device");
        System.out.println("3) Sniff (1 Paket)");
        System.out.print("Auswahl: ");
    }

    private static void listDevices(List<PcapIf> devices) {
        for (int i = 0; i < devices.size(); i++) {
            PcapIf device = devices.get(i);
            Optional<String> description = device.description();
            System.out.printf("[%d] %s%s%n",
                    i,
                    device.name(),
                    description.map(d -> " (" + d + ")").orElse(""));
        }
    }

    private static int selectDevice(List<PcapIf> devices, Scanner scanner, int currentIndex) {
        System.out.print("Index eingeben: ");
        String idx = scanner.nextLine().trim();
        try {
            int parsed = Integer.parseInt(idx);
            if (parsed < 0 || parsed >= devices.size()) {
                System.out.println("Ungueltiger Index.");
                return currentIndex;
            }
            System.out.println("Ausgewaehlt: " + devices.get(parsed).name());
            return parsed;
        } catch (NumberFormatException ex) {
            System.out.println("Bitte eine Zahl eingeben.");
            return currentIndex;
        }
    }

    private static void sniff(List<PcapIf> devices, int selectedIndex) throws PcapException {
        PcapIf selected = devices.get(selectedIndex);
        System.out.println("Sniffe auf: " + selected.name());
        try (Pcap pcap = Pcap.create(selected)) {
            pcap.setSnaplen(65535)
                    .setPromisc(true)
                    .setTimeout(1000)
                    .activate();

            // Sniffe unendlich viele Pakete, bis das Programm manuell gestoppt wird
            pcap.loop(-1, (String msg, PcapHeader header, byte[] packet) -> {
                try {
                    System.out.printf("ts=%d caplen=%d wirelen=%d bytes=%d%n",
                            header.toEpochMilli(),
                            header.captureLength(),
                            header.wireLength(),
                            packet.length);
                } catch (Exception e) {
                    System.out.println("Fehler: " + e.getMessage());
                }
            }, "Capture");
        }
    }
}
